# Repository Guidelines

## Project Overview

OptiRefine replaces OptiFine's destructive class transformer with Mixin-based compatibility repairs for Minecraft 1.12.2 on Cleanroom. It also backports selected modern shaders, CEM expressions, and resource paths. The tested baseline is Java 25, Cleanroom 0.5.17-alpha, and OptiFine HD U G5. The project remains Runtime Alpha; compatibility beyond this baseline requires separate verification.

## Architecture & Data Flow

- `OptiRefineCore` delegates transformer registration to `OptiRefineBootstrap`. Launch transformers disable OptiFine's original patching, repair its Forge integration, and map typed Config references.
- `src/main/resources/optirefine.modules.json` is the authoritative ordered default-mixin inventory. `DefaultMixinPlugin` validates dependencies and selects common blocks followed by the current side's blocks. Preserve the required common `g5-base` block. `ModMixinPlugin` handles optional mod-specific mixins.
- Completed Mixin targets, including late MixinExtras injections, flow through `MixinPostProcessor`: Cursed Mixin Extension lowering, Config facade remapping, residual-reference verification, then diagnostic dumping. Preserve this order.
- `optifine/Config.java` is a typed compile-time facade; its native declarations are rewritten to the real runtime Config. Cursed annotations and shadow bodies also represent transformation contracts. Change the corresponding lowering/mapping when changing these surfaces.
- `MixinModernShaderPipeline` connects G5 lifecycle/render hooks to `ModernShaderSource`, `ModernShaderPipeline`, and `ShaderPackState`. Pack state owns compute programs and modern stage/shadow-alternate resources; G5 framebuffers and textures are borrowed. Cleanup follows pack reload/uninit and must preserve borrowed resources.

## Key Directories

Java package root: `src/main/java/mods/Hileb/optirefine/`.

- `core/`: bootstrap, mod metadata, transformers, module planning, and postprocessing.
- `mixin/defaults/` and `mixin/mods/`: target-package mixins and optional mod integrations.
- `optifine/`: Config facade, modern shader/resource support, CEM, and client settings.
- `library/cursedmixinextensions/`: custom annotations and bytecode lowering.

Other repository paths:

- `src/main/resources/`: module manifest, Mixin configs, access transformer, and assets. `src/main/resource-templates/` contains pack/mod metadata templates.
- `src/test/java/`: JVM bytecode, module, facade, artifact-boundary, and parser contracts.
- `src/clientTest/`: isolated native-client validation mod and fixtures. Editable shader packs live in `resources/fixtures/shaderpacks/<pack>/shaders/`.
- `gradle/scripts/`: dependency, verification, publishing, and auxiliary Gradle logic. Build and release workflows live in `.github/workflows/`.

## Development Commands

Run from the repository root. On Windows use `.\gradlew.bat` in place of `./gradlew`.

| Command | Purpose |
| --- | --- |
| `./gradlew build` | Compile, remap, package, and run enabled JVM contracts. |
| `./gradlew test` | Run JUnit contracts, including their runtime-artifact dependency. |
| `./gradlew runClient` / `./gradlew runServer` | Launch configured Cleanroom development instances. |
| `./gradlew prepareClientContractFixtures` | Generate reproducible fixture ZIPs without launching Minecraft. |
| `./gradlew verifyClientContracts` | Run isolated MCP development-client contracts. |
| `./gradlew verifyRuntimeClientContracts` | Run final SRG Jar contracts with production Minecraft/Cleanroom and official G5. |
| `./gradlew build stageBuildArtifacts` | Build and stage deliverables in `build/distributions/artifacts/`. |

No dedicated lint or formatter task is configured. Match surrounding style.

## Code Conventions & Common Patterns

- Use four-space Java indentation, existing brace style, camelCase members, uppercase constants, and `optiRefine$` names for injected helpers/state. Preserve required OptiFine member names for compatibility bridges.
- Compile against MCP names. Use existing `@AccessibleOperation`/Cursed extension patterns for OptiFine-only members; OptiFine string targets generally use `remap = false`. Access-transformer rules use MCP names and are remapped during packaging.
- Register default mixins and dependencies in the module manifest; processed Mixin lists are generated from it. Keep client rendering classes in client blocks and common/server paths free of client linkage.
- Initialize added mixin instance state through the established constructor injection pattern, such as `<init>*` at `RETURN`; ordinary field initializers are not sufficient for this mixin environment.
- Use Log4j parameterized messages and retain target/phase context in transformation failures. Shader failures must use existing pack-off/recovery and cleanup paths.
- Restore render and entity/world contexts with `try/finally`, including nested and exceptional calls. Keep GL operations in the client render lifecycle; preserve ownership, sized image storage, barriers, texture flips, and sampler refreshes. Avoid per-frame allocations and redundant GL work.

## Important Files

Under the Java package root, start with `core/OptiRefineCore.java`, `core/OptiRefineBootstrap.java`, `core/mixin/MixinModulePlan.java`, `core/mixin/MixinPostProcessor.java`, and `core/transformer/ConfigFacadeMapper.java`.

- `src/main/resources/optirefine.modules.json`, `optirefine.default.mixin.json`, `optirefine.mods.mixin.json`, and `optirefine_at.cfg`: selection, plugins/refmap, and access rules.
- `build.gradle`, `gradle.properties`, and `settings.gradle`: build/runtime versions, feature flags, and repositories. `gradle/scripts/verification.gradle` owns client isolation, fixture generation, remapping, and artifact staging.
- `src/clientTest/java/mods/Hileb/optirefine/validation/ClientContracts.java` and `ShaderLifecycleContracts.java`: real-client assertions and resource-lifecycle checks.
- `docs/technical-notes.md`: detailed architecture, compatibility boundaries, and recorded verification. `README.md`: supported baseline and user commands.

## Runtime/Tooling Preferences

Use the checked-in Gradle 9.6.1 wrapper and JDK 25 to match CI. Source/target are Java 25; the Gradle JVM itself is not toolchain-pinned. Unimined supplies MCP stable 39-1.12 mappings and remaps the official G5 input. The `-dev.jar` is for development; deploy the final SRG runtime Jar with official OptiFine. Unpatched/decompiled cache Jars are not distribution artifacts.

Native suites require OpenGL 4.3+, a graphical desktop, and a focusable window. Linux CI uses Xvfb, Openbox, and Mesa llvmpipe (`LIBGL_ALWAYS_SOFTWARE=1`, `GALLIUM_DRIVER=llvmpipe`). Use portable paths; keep generated ZIPs, build/cache directories, game instances, and logs out of source changes. Edit fixture sources, not generated archives.

## Testing & QA

- JVM tests use JUnit Jupiter 6/JUnit Platform. Bytecode changes need executable linkage/namespace coverage, including MCP and SRG where relevant. No numeric coverage threshold is configured; add regressions for observable failures rather than assertions about source wording or wiring.
- Run both native suites for rendering, shader, resource, or classloader changes. Development validation loads the development and test Jars through Forge discovery; production validation checks the final SRG artifact without development outputs/flags. Preserve these boundaries.
- Shader contracts assert actual pixels, stage selection, resize/reload recovery, owned-resource deletion, and borrowed-texture survival. `modern-failure` and runtime-generated `modern-resize-failure` deliberately fail to test recovery; retain those expectations.
- Inspect `build/reports/tests/test/`, `build/reports/client-contracts/`, and `build/reports/runtime-client-contracts/` (including production `launch.json`, logs, and captures). A JVM pass does not prove native behavior; local driver results do not establish Linux CI or arbitrary pack/mod compatibility.
- Both client suites automatically prepare fixture ZIPs under `build/client-contract-fixtures/`. Release workflows run build/native verification and create a draft with runtime/source Jars and checksums. Keep sources-Jar generation enabled for that workflow.
