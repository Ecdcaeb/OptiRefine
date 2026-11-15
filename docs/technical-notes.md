# Technical notes

[Project overview](../README.md)

## Build internals

Note: mixins are written against the vanilla MCP classes on the compile classpath;
OptiFine-only members are accessed via `@AccessibleOperation`

Compilation and `runClient` share a dedicated `optifine` dependency, remapped from the official G5
namespace to MCP with Minecraft's class hierarchy. The hand-decompiled `_unpatched_srg` artifact is a
compilation reference and cannot be used as a runtime distribution.

The packaged `mods.Hileb.optirefine.optifine.Config` type is a compile facade. Typed ASM mapping
redirects its references to OptiFine's default-package `Config`, including descriptors, signatures,
annotations, handles and dynamic constants. Ordinary OptiRefine helper classes are handled without
individual target registration; completed mixin targets use the same mapper after extension lowering.
Bootstrap/library classes are checked for forbidden facade dependencies in the runtime artifact.

Mixin-owned handlers and internal helpers use `optiRefine$` followed by an action and its purpose,
such as `optiRefine$drawExperienceTextWithCustomColor`. Constructor aliases, explicit-descriptor
access bridges and access-control markers follow the same convention; access-only fields end in
`Access`. Native members, exported OptiFine APIs and interface implementations retain their bound
names. Soft-interface prefixes such as `srrl$` remain separate so Mixin does not mistake ordinary
handlers for interface methods. Parameter names describe their role; annotation target names and
descriptors continue to identify the original runtime members.

The naming refactor is checked against the compiled baseline: 153 mixin classes, 1,286 methods
and 732 fields retain identical instructions, constants, annotation targets and bound/exported
contracts after normalizing renamed identifiers. The comparison covers 508 method-symbol and
22 field-symbol renames; its report is `build/reports/mixin-naming/bytecode.txt`.

The renamed build also passes real-client startup, debug text, normal/Fast Render, zoom release,
internal shaders on/off, video/skin settings and world exit. Controlled render failures check
matrix/lightmap/model context restoration, texture bindings, sprite ordering and display-list
cleanup. Results and captures are in `build/reports/mixin-naming/`.

## Core architecture

`OptiRefineCore` is the FML callback adapter. `OptiRefineBootstrap` owns transformer registration
and the launch source; `ModMetadataLoader` reads folder/JAR metadata independently, and
`OptiRefineModContainer` adapts that metadata and source to Forge. Default and optional-mod Mixin
policies live in `core.mixin`.

`MixinPostProcessor` tracks pending targets by `ClassNode` identity and processes each completed
target from `IExtension.export`, after MixinExtras' late injectors. Its order is extension lowering,
Config facade mapping, residual-reference verification, then final diagnostic output. The extension
engine has ordered class, method, cleanup, field-alias and call-rewrite phases. Failures retain the
original cause and report the class, phase, namespace status, member and annotation.

`optirefine.modules.json` is the authoritative ordered mixin list. It declares G5 base, platform
repairs, modern resource/CEM/shader groups and their shared client lifecycle dependency. Resource
processing generates the default configuration's static common/client/server arrays so Unimined
can remap every entry; the runtime plugin validates the selected side's dependencies. Common
entries precede side entries, and the baseline order of 17 common/server and 152 total client
mixins is preserved. These groups do not introduce feature-disable switches.

`ShaderPackState` owns one pack's compute programs, prepare/shadowcomp custom textures and shadow
alternates. G5 color/depth targets and gbuffers textures are borrowed. Closing a state releases
owned resources once and clears borrowed references; the program registry, uniform objects and
capability buffers retain game lifetime. Pack states are created at load boundaries, not per frame.

The architecture cutover passes 26 persistent JVM contracts (`./gradlew build`), including
generated-class execution, constructor helpers, alias chains, MCP/SRG selection, late-added calls,
facade handles and runtime-artifact reference boundaries. JVM results are in
`build/reports/tests/test/`.

`./gradlew verifyClientContracts` launches an isolated client under `run/client-contracts` and
requires OpenGL 4.3. On the documented G5 baseline, settings, resource/model priority, sorting snapshots,
nine shader fixtures, resize, pack reload and owned/borrowed native resource lifetimes pass.
Native results and captures are in `build/reports/client-contracts/`; the exact baseline mixin
membership/order proof is in `build/reports/architecture/module-order.txt`.

## Runtime optimizations

Modern `prepare` and `shadowcomp` passes build ordered active-program arrays when a pack finishes
loading. Empty groups return without scanning their 100 registry slots. Compute-only programs stay
in these arrays; reload replaces them, and resize retains the same programs and execution order.

The `Shaders.useProgram` return hook binds image textures even when the requested program is
already active. Compute and modern stage callers use that binding instead of repeating it.
Image/texture memory barriers and final state restoration remain in place.

Quad sprite sorting reuses a builder-owned scratch array, grows it only when required and clears
its used entries after sorting. Saved vertex states retain independent sprite arrays. Real-client
checks cover repeated sorting, growth, builder isolation and snapshot restoration.

Face orientation uses scalar vector arithmetic and a private cached facing array. Expanded Forge
quad normals use the same scalar cross product and vecmath reciprocal normalization; compact
quads retain Forge's original call. Floating-point operation order, direction ties and degenerate
or non-finite inputs retain their existing results. Real-client geometry contracts compare both
vertex layouts against the vector implementations and check unrelated vertex attributes.

Native-client instrumentation against baseline `2ceffbd` records:

| Probe | Before | After |
| --- | ---: | ---: |
| Allocated bytes for 100,000 face-orientation calls | 4,000,056 | 56 |
| Image bindings for 300 compute dispatches | 4,800 | 2,400 |
| Program checks for 100 calls to each empty stage group | 20,000 | 0 |

The compute samples retain 900 image/texture memory barriers. The Java 25 build passes 26 JVM
contracts; the real client passes 8,208 geometry cases and all nine shader fixtures, including
resize, reload and owned/borrowed resource lifetimes. Raw samples and methodology are in
`build/reports/performance-20261115/`; client captures remain in `build/reports/client-contracts/`.
These are allocation and call-count measurements, not an FPS benchmark. The isolated test fixture
disables the splash screen so startup warnings are logged without waiting for a keypress.

Shader metadata checks reuse compiled dispatch patterns. Macro expansion allocates its output
builder only after finding a replacement, retaining quoted replacements and the 16-pass limit.
Parser checks cover unchanged lines, chained macros, fixed/relative groups and malformed input.

Modern/legacy resource name translations are cleared when Minecraft starts a resource refresh.
Names requested during loading remain cached until the next refresh. Real-client checks cover
cache eviction and unchanged ZIP/folder lookup, same-pack precedence and cross-pack priority.

Field access transformers collect ordered alias replacements, then rewrite field references in
one class-wide instruction walk. Eight generated-class cases retain byte-identical output to the
previous processor, covering alias chains, descriptors, repeated targets, foreign owners and
MCP/SRG matching; a generated chained alias also passes JVM linkage and execution.

Debug HUD performance rows are updated in one independent mutable list, avoiding an intermediate
stream result. Actual client checks verify the animation suffix and that Forge-style additions
do not leak into later calls; captures verify the rendered debug overlay.

Optimization validation results and captures are in `build/reports/optimization-proof/`.

## Runtime validation

Baseline: **Minecraft 1.12.2**, **Java 25.0.5**, **Cleanroom 0.5.17-alpha** and **OptiFine HD U G5**.

Verified in this baseline: cold startup, singleplayer entry, zoom release, normal/Fast Render,
internal shaders on/off, video/skin settings and world exit. Runtime fault injection also checks
matrix depth/content, texture/lightmap restoration, nested model/emissive context, quad/sprite
ordering and shader buffer growth/view reuse. Captures and assertions are in `build/reports/audit-runtime/`.

The Forge cutover is audited in both MCP and SRG namespaces across 481 OptiFine runtime classes;
508 transformed method bodies pass ASM stack verification, with no remaining references to the
removed Forge reflection metadata. The caller conversion removes 11 reflection argument arrays
and four primitive boxing operations.

The client checks exercise state-aware block/light/model overrides, custom item/map overrides,
entity registry aliases, chunk-cache side checks, spawn event result precedence and cancellation,
crash details, resource lookup, mod IDs and Forge custom item model loading. G5 custom item test
packs use `mcpatcher/cit/`.

Forge verification reports and captures are under `build/reports/forge-direct-*/`.

Modern backport validation also passes on **OpenGL 4.6.0 / NVIDIA 582.78** with the baseline
above. Nine shader fixtures check actual framebuffer pixels for ordered compute suffixes,
compute-only composite/deferred, includes/options, render-quality resizing, fixed/relative/zero
workgroups, program conditions, prepare/shadowcomp graphics ping-pong and graphics image reads
from image unit 5. Native program deletion, shadow-alternate cleanup, image unbinding and
pack-off/recovery are asserted. One fixture deliberately fails compilation to exercise recovery.

ZIP/folder resource fixtures check both roots, same-pack precedence, cross-pack priority and
deduplication, then validate four real CIT model selections. A rendered pig JEM checks persistent,
independent float/boolean variables and nested context restoration. Results and captures are in
`build/reports/modern-backport/`.

## Modern OptiFine backports

The G5 runtime now accepts compute shader programs (`.csh` and `_a.csh` through `_z.csh`),
including compute-only deferred/composite passes. Compute shaders require **OpenGL 4.3**;
image load/store in graphics shaders requires **OpenGL 4.2**. Minecraft's compatibility-profile
renderer remains in use. Unsupported capabilities, image formats and invalid modern programs
produce a shader-pack error and use G5's existing pack-off path.

`prepare` and `shadowcomp` stages, through suffix `99`, share the existing program registry,
dimension overrides, conditions, options, includes and uniform updates. Image units map
`colorimg0..5` to current main textures and `shadowcolorimg0..1` to current shadow textures.
Compute dispatch supports fixed `workGroups` and render-relative `workGroupsRender`, follows
graphics ping-pong changes and applies image/texture barriers before later consumers. Owned
programs, custom stage textures and shadow alternates are released on uninit/reload; client
shutdown uninitializes shaders before destroying the GL context.

CEM adds `lerp`, boolean `ifb`, seeded `random`, `day_time` and `day_count`, plus real frame,
dimension, player position/rotation, entity rotation/ID and death-time parameters. `var.*` float
and `varb.*` boolean variables belong to each entity and are resolved through nested render
scopes. Block entity models reject entity-variable namespaces. Existing G5 expressions remain
available; functions and variables retain their float/boolean type checks.

Modern `optifine/` resource paths are accepted alongside `mcpatcher/`. Resolution preserves
resource-pack priority, prefers the modern path within a pack containing both, and deduplicates
logical rules. ZIP and folder packs, discovery and relative CIT/CTM references share this rule.

These additions retain G5's eight color buffers and the 1.12.2 scene/model APIs. They do not
establish compatibility with arbitrary newer shader packs, resource formats or newer entities.

## Compatibility repairs

- [#2](https://github.com/Ecdcaeb/OptiRefine/issues/2): reject back-facing polygons in the living-entity
  glow pass. Fast Render retains its depth-disabled through-wall silhouette behavior.
- [#5](https://github.com/Ecdcaeb/OptiRefine/issues/5): reset the real, remapped mouse-filter fields when
  leaving zoom, avoiding references to removed access placeholders.
- [#6](https://github.com/Ecdcaeb/OptiRefine/issues/6): preserve writable, native-order shader float
  views from byte zero, reusing them until the underlying buffer actually grows.
- [#7](https://github.com/Ecdcaeb/OptiRefine/issues/7): keep the original spawning method and its
  injection points while reusing candidate coordinates and uninitialized samples through narrow hooks.

Rendering additions restore their owned matrix, texture, lightmap, shader and emissive scopes after
exceptions and nested calls. Custom-model context is restored to its enclosing renderer; zero model
scales no longer require reciprocal scaling, and failed sprite compilation closes its display list.
Other repairs cover shader vertex formats/caches, texture/resource lifetimes, chunk/VBO ownership,
settings persistence and constructor initialization order.

Forge rendering/spawning hooks and custom vertex formats remain in the execution path. Exception
cleanup and resource-lifetime repairs intentionally correct inherited G5 defects as well as porting
errors. These changes retain Runtime Alpha status; arbitrary mod combinations, resource packs and
external shader packs need their own verification.

OptiFine G5's Forge integration uses direct method calls and field access for custom item models,
connected textures, chunk caches, shader passes and Forge helpers. The Forge reflection metadata
table is removed; these calls no longer allocate reflection argument arrays or box primitive
arguments. The remaining vanilla-model and optional-mod reflectors retain deferred resolution.
This conversion targets G5's bytecode and the Cleanroom API above; changed layouts fail explicitly.


