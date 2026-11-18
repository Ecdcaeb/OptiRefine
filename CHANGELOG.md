# Changelog

## Unreleased

- Fix G5 chunk meshes using a stale block-light value when callers query the same position with different minimum light levels.
- Apply shader entity tags to copied vertices and leave shared baked arrays and bulk vertex inputs untouched.
- Discover compute shaders from ZIP entries and share the result between option scanning and program setup.
- Size G5 render caches to the padded block bounds and preserve backing-cache lookups outside them.
- Publish immutable dynamic-light samples so chunk workers can evaluate lighting without the G5 update lock.

## 0.2.0-alpha

The tested baseline is Minecraft 1.12.2, Java 25, Cleanroom 0.5.17-alpha, and OptiFine HD U G5. [Changes since 0.1.0-alpha](https://github.com/Ecdcaeb/OptiRefine/compare/v0.1.0-alpha...e74a9bce88339d35ee9d4b52854eee55f5885031)

### Modern OptiFine features

- Backported compute shaders, including compute-only composite and deferred passes, fixed and render-relative workgroups, and the `prepare` and `shadowcomp` stages. Compute requires OpenGL 4.3. Graphics shader image load/store requires OpenGL 4.2.
- Added CEM functions such as `lerp`, `ifb`, seeded `random`, `day_time`, and `day_count`, along with live render parameters and per-entity `var.*` and `varb.*` storage. Block entities rendered as items can evaluate CEM expressions using the enclosing render world or client world.
- Added modern `optifine/` resource paths alongside `mcpatcher/` for ZIP and folder packs. Lookup preserves pack priority and prefers the modern path within the same pack.

### Compatibility and fixes

- Fixed startup with Nothirium and VintageFix. With RenderLib installed, entities and block entities render in the normal and shader shadow passes. Multi-texture sprites follow Nothirium's quad sort, including with anisotropic filtering. Verified the combination of Nothirium 0.4.9-beta, RenderLib 1.4.5, and VintageFix 0.7.2 on Cleanroom 0.7.14-alpha with G5. [#8](https://github.com/Ecdcaeb/OptiRefine/issues/8)
- Preserved Universal Tweaks' block rotation parsing and Forge's vertex color handling. Resolved Kirino headless rendering settings through the configuration API available in the installed Cleanroom version.
- Fixed image texture storage and sampler updates for modern shader packs. Failed compilation or resizing returns to shaders off and releases pack-owned resources while preserving the game framebuffer.
- Restored local `/reloadChunks` and `/reloadShaders` completion and history, one-shot G5 chat notices, and F3+L visible chunk loading.

### Performance and validation

- Skip inactive shader stages and reuse image bindings, quad sprite sorting storage, and cached geometry data. Anisotropic filtering, custom items, and alternate blocks can reload just textures and models when Forge selective resource reload is enabled.
- Replaced G5's Forge reflection calls with direct integration and consolidated the default mixin inventory. Builds now verify runtime bytecode boundaries, and release jobs exercise both the packaged MCP development client and the final SRG runtime with reproducible shader fixtures. [Technical notes](docs/technical-notes.md)
