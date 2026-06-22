# Hemodynamics

A [Create](https://modrinth.com/mod/create) add-on and library that adds a **Blood** fluid and the means to extract it. Blood is exposed as a standard fluid with common tag `c:blood` and a small, stable API.

## Features

- **Blood** — a standard, placeable fluid with lava interaction (flowing lava + blood → netherrack)
- **Blood extraction** — mobs killed by **Crushing Wheels** deposit blood into a **Basin** or **Item Drain** placed below. This is scaled by max health and mob type.
- **Library-friendly** — public API + documented, mergeable tags.

## Requirements

- Minecraft 1.21.1
- NeoForge
- Create 6.0+

## Configuration

Server config: `serverconfig/hemodynamics-server.toml`

| Option | Default | Description                                                       |
|--------|---------|-------------------------------------------------------------------|
| `blood_harvest.mbPerHealth` | `50` | Amount of blood (mB) per point of max health, before mob scaling. |


## Tags

All tags are additive (`"replace": false`), so other mods and datapacks can contribute entries by shipping a file at the same path.

### Fluid tags

| Tag ID | Contains | Purpose |
|--------|----------|---------|
| `c:blood` | `hemodynamics:blood`, `hemodynamics:flowing_blood` | Common tag for the blood fluid. Reference this instead of the raw fluid id. |

### Entity type tags (blood-harvest scaling)

Determines how much blood a mob yields when killed by Crushing Wheels, as a fraction of `maxHealth * mbPerHealth`.

| Tag ID | Yield | Default contents                          |
|--------|-------|-------------------------------------------|
| `hemodynamics:blood/trace`  | 10%  | bogged, husk                              |
| `hemodynamics:blood/low`    | 25%  | `#minecraft:undead` (excluding skeletons) |
| `hemodynamics:blood/medium` | 50%  | arthropods,  creepers, endermen           |
| `hemodynamics:blood/full`   | 100% | villagers, animals, etc.                  |

**Resolution order:** `blood/trace` → skeletons (0%) → `blood/medium` → `blood/low` → `blood/full` → `Animal` instances default to 100% → otherwise 0%.

### Adding your mob

Ship this in your mod jar or a datapack:

`data/hemodynamics/tags/entity_type/blood/full.json`

```json
{
  "replace": false,
  "values": ["yourmod:giant_cow"]
}
```

## For developers

> Tag IDs and the `api` package are part of the public contract and will not change without a major version bump. Do **not** use `Hemodynamics.REGISTRATE` — create your own `CreateRegistrate` instance.

### Gradle dependency

```gradle
repositories {
    maven { url = "https://api.modrinth.com/maven" } // or CurseMaven
}
dependencies {
    implementation "maven.modrinth:hemodynamics:<version>"
}
```

### Using blood in code

```java
import net.midnight.hemodynamics.api.BloodFluids;

FluidStack blood = BloodFluids.of(1000);          // 1000 mB of blood
Fluid source = BloodFluids.source();              // hemodynamics:blood
boolean isBlood = fluidState.is(BloodFluids.TAG); // c:blood
```

### Depending on Hemodynamics

In your `neoforge.mods.toml`:

```toml
[[dependencies.yourmodid]]
    modId = "hemodynamics"
    type = "required"   # use "optional" for a soft dependency
    versionRange = "[1.0.0,)"
    ordering = "AFTER"
    side = "BOTH"
```

For soft compatibility with no hard dependency, reference the `c:blood` tag.

## License

Hemodynamics is licensed under the [MIT License](LICENSE).