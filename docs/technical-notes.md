# Technical notes

[Project overview](../README.md)

## Build internals

Note: mixins are written against the vanilla MCP classes on the compile classpath;
OptiFine-only members are accessed via `@AccessibleOperation`

Compilation and `runClient` share a dedicated `optifine` dependency, remapped from the official G5
namespace to MCP with Minecraft's class hierarchy. The hand-decompiled `_unpatched_srg` artifact is a
compilation reference and cannot be used as a runtime distribution.

The packaged `mods.Hileb.optirefine.optifine.Config` type is a compile facade. Mixin post-processing
redirects its uses to OptiFine's default-package `Config`; ordinary helper classes need an explicit
transformer target before using that facade.

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


