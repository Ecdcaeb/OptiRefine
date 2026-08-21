# OptiRefine — Runtime Alpha Test

> **Status: Runtime Alpha Test**. You can download and try it out, but it is **not recommended** for
> important worlds or servers; crashes or rendering issues may still occur. **Issues are accepted** — bug
> reports about crashes or errors caused by OptiRefine are welcome. Note: rendering anomalies while shaders
> are active are expected — don't worry, it is normal.

<img width="683" height="293" alt="a6fade3b-b7ea-4651-a2a5-a69ffbf996b0" src="https://github.com/user-attachments/assets/4ec54838-aa21-4877-a38f-d50d8e11f554" />

---

OptiRefine is a mod that utilizes Mixin to patch Optifine, keeping optifine compatible with cleanroom and reducing its destructiveness.

Let's enjoy the great optifine with the shaders

<img width="854" height="480" alt="2026-08-21_14 20 25" src="https://github.com/user-attachments/assets/62d83862-c162-4ea2-bd24-0b74165beec4" />

[Go and get Mellow](https://modrinth.com/shader/mellow/versions?g=1.12.2) which i like

Post incompatible issues of optifine here!

## Build

The project targets **Java 25**

```
./gradlew build
```

Note: mixins are written against the vanilla MCP classes on the compile classpath;
OptiFine-only members are accessed via `@AccessibleOperation` / `@Shadow` (see FOR_AGENT.md).

## Test Environment

- Cleanroom `0.6.9-alpha` + `OptiFine_1.12.2_HD_U_G5.jar` (or you like `OptiFine_1.12.2_HD_U_G6_pre1.jar` )
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

## Fixed Issue under Cleanroommc/Cleanroom

https://github.com/CleanroomMC/Cleanroom/issues/600

https://github.com/CleanroomMC/Cleanroom/issues/513

https://github.com/CleanroomMC/Cleanroom/issues/359

https://github.com/CleanroomMC/Fugue/issues/123

https://github.com/CleanroomMC/Fugue/issues/113
