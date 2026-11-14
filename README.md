# <img width="56" height="56" alt="image" src="https://github.com/user-attachments/assets/ffd17053-9d3a-49a6-adf7-dc158776ddb4" /> OptiRefine 0.1.0-alpha — Runtime Alpha Test

> **Status: Runtime Alpha Test**. You can download and try it out, but it is **not recommended** for
> important worlds or servers; crashes or rendering issues may still occur. **Issues are accepted** — bug
> reports about crashes or errors caused by OptiRefine are welcome. Note: rendering anomalies while shaders
> are active are expected — don't worry, it is normal.

---



OptiRefine is a mod that utilizes Mixin to patch Optifine, keeping optifine compatible with cleanroom and reducing its destructiveness.

Let's enjoy the great optifine with the shaders. 
Go and get [Mellow](https://modrinth.com/shader/mellow/versions?g=1.12.2) which i like

<img width="854" height="480" alt="2026-08-09_03 36 39" src="https://github.com/user-attachments/assets/ffab29d1-2a00-443b-9a9d-1206e1504d2e" />

## Build

The project targets **Java 25**

```
./gradlew build
```

## Installation

- Tested with **Java 25**, **Cleanroom 0.5.17-alpha** and **OptiFine 1.12.2 HD U G5**.
- Deployment: put the official OptiFine jar and `build/libs/optirefine-0.1.0-alpha.jar` in the instance `mods/` folder.
- Other Cleanroom/OptiFine versions, including G6_pre1, require separate runtime verification.
- Bug reports: attach `logs/latest.log` / `crash-reports/crash-*.txt`

Implementation notes, compatibility repairs and validation details are in [Technical notes](docs/technical-notes.md).

## Feats

### Cleanroom removed Realms

| Configuration | Image | Note |
| --- | --- | --- |
| with Fugue | ![image](https://github.com/user-attachments/assets/9ac12224-b8dd-42f0-ba33-2975854a0fef) | — |
| with OptiRefine | ![image](https://github.com/user-attachments/assets/2c701e4b-1325-4b61-a5c7-5308e7d34528) | OptiRefine does not perform any special repairs, which may prove to be universal. |




## OptiFine Great Again

For a decade, OptiFine was the reason Minecraft didn't just run — it *looked*. Shaders, smooth lighting, zoom, dynamic skies: the game you remember is the game OptiFine drew. Then the ecosystem moved on. Cleanroom replaced the old Forge, and OptiFine's blunt bytecode surgery no longer fit in the house it used to own.

OptiRefine is the rebuild — not a patch on a patch, but a faithful re-implementation: 135 mixins restore OptiFine's behavior on Cleanroom the way it should have been done from day one, with the destructive class transformer switched off for good.

The features are back. The compatibility is back. The look is back.

Make OptiFine great again.

## Fixed Issue under Cleanroommc/Cleanroom

<details>
<summary>Issues</summary>

- [Cleanroom #600](https://github.com/CleanroomMC/Cleanroom/issues/600) — `Crashes when used with OptiFine`
- [Cleanroom #513](https://github.com/CleanroomMC/Cleanroom/issues/513) — `Optifine make the game crash in full screen` 
- [Cleanroom #359](https://github.com/CleanroomMC/Cleanroom/issues/359) — `Instant crash on nearly vanilla setup (0.3.4)`  
- [Fugue #123](https://github.com/CleanroomMC/Fugue/issues/123) — `Optifine breaks "Remove Realms Button" patches when Fugue is installed`
- [Fugue #113](https://github.com/CleanroomMC/Fugue/issues/113) — `[Optifine] Fullscreen Mode doesn't move.`  

</details>
