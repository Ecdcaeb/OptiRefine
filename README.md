# OptiRefine — Runtime Alpha Test

> **状态：Runtime Alpha Test**（运行时 alpha 测试）。可下载体验，但**不建议**在存档/服务器等重要场景使用；
> 可能仍有崩溃或渲染问题。暂不接受由 OptiRefine 引发崩溃或错误的 issue；但你仍然可以反馈
> OptiFine 本身在 Cleanroom 环境下存在的问题与需要的特性。

# OptiRefine
OptiRefine is a mod that utilizes Mixin to patch Optifine, keeping optifine compatible with cleanroom and reducing its destructiveness.

Post incompatible issues of optifine here!

## Build

The project targets **Java 25**; the local build environment runs **JDK 26** (`JAVA_HOME` must point to it). Then:

```
./gradlew build
```

Note: mixins are written against the vanilla MCP classes on the compile classpath;
OptiFine-only members are accessed via `@AccessibleOperation` / `@Shadow` (see FOR_AGENT.md).

## Test Environment

- Cleanroom `0.6.7-alpha` + `OptiFine_1.12.2_HD_U_G6_pre1.jar`
- Deployment: copy `build/libs/optirefine-0.0.1-indev.jar` into the instance `mods/` folder
- Bug reports: attach `logs/latest.log` / `crash-reports/crash-*.txt`

## Feats

### Cleanroom removed Realms
OptiRefine does not perform any special repairs, which may prove to be universal.

with Fugue

<img width="851" height="477" alt="image" src="https://github.com/user-attachments/assets/9ac12224-b8dd-42f0-ba33-2975854a0fef" />

with OptiRefine

<img width="833" height="507" alt="image" src="https://github.com/user-attachments/assets/2c701e4b-1325-4b61-a5c7-5308e7d34528" />

## OptiFine Great Again

For a decade, OptiFine was the reason Minecraft didn't just run — it *looked*. Shaders, smooth lighting, zoom, dynamic skies: the game you remember is the game OptiFine drew. Then the ecosystem moved on. Cleanroom replaced the old Forge, and OptiFine's blunt bytecode surgery no longer fit in the house it used to own.

OptiRefine is the rebuild — not a patch on a patch, but a faithful re-implementation: 125 mixins restore OptiFine's behavior on Cleanroom the way it should have been done from day one, with the destructive class transformer switched off for good.

The features are back. The compatibility is back. The look is back.

Make OptiFine great again.
