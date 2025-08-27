# Mythic Ritual

A lightweight, data‑driven ritual crafting system for NeoForge (MC 1.21.1). You perform a ritual by right‑clicking a target block with a trigger item while the required ingredient entities (items and/or mobs) are placed at specific relative offsets around the block. If all constraints match, the mod consumes the inputs, plays an optional effect (particles / lightning / none), and spawns the result item.

> Status: Early WIP – JSON format and behavior may change.

## Core concepts
- Target block: The block you click (its full BlockState must match).
- Trigger item: ItemStack in your hand (can optionally be consumed or damaged via `consumeTrigger`).
- Ingredients: Nearby entities (item stacks on ground or living mobs) at precise rotated offsets.
- Rotation: Offsets are written assuming the player faces NORTH when defining the recipe. At runtime they rotate with the player's facing (N/E/S/W).
- Effects: Visual / audio feedback (`none`, `particles`, `lightning`).
- Constraints: Optional dimension whitelist, sky visibility requirement.

## JSON recipe location
Place files under:
```
data/<namespace>/recipes/<name>.json
```
Use `"type": "mythicritual:ritual_recipe"`.

## Field reference
| Field | Type | Required | Description |
|-------|------|----------|-------------|
| type | string | yes | Always `mythicritual:ritual_recipe` |
| target | BlockState | yes | Block you click. Example: `{ "Name": "minecraft:stone" }` |
| trigger | ItemStack | yes | ItemStack in hand. Format uses vanilla ItemStack codec (`id`, optional `Count`, optional `tag`). |
| ingredients | array | yes | List of ingredient objects (item or mob). |
| result | ItemStack | yes | Spawned ItemStack (copied). |
| dimensions | array(ResourceKey) | no | Allowed dimensions. Empty/omitted = any. |
| effect | string | no | `none` (default), `particles`, `lightning`. |
| needSky | bool | no | Require open sky above origin. Default `false`. |
| consumeTrigger | bool | no | If true the trigger stack (or  durability if damageable) is consumed. |

### Ingredient objects
Two discriminator forms share the same `type` field:
1. Item ingredient: `type` points to an ITEM id in vanilla registry. Optional `quantity` (default 1).
2. Mob ingredient: `type` points to an ENTITY_TYPE id (and is NOT an item id). Always consumes (kills) the mob.

Common optional field: `pos` (a block offset as `[x, y, z]` array or object – the Vec3i codec accepts object `{ "x":0, "y":1, "z":0 }` or list). Default is `[0,1,0]` (one block above the origin). Offsets are rotated from NORTH to the player facing.

Item ingredients track partial consumption: if an item entity holds a stack of 32 and two ingredients each require 8 of that same stack, the mod increments usage and only shrinks once.

## Example 1: Simple transmutation with particles
Transforms 4 redstone dust plus an iron ingot (trigger) on a stone block into a compass. Consumes the ingot. Redstone item entities must be placed in a cross around the block at ground level; player may face any direction when activating.
```json
{
  "type": "mythicritual:ritual_recipe",
  "target": { "Name": "minecraft:stone" },
  "trigger": { "id": "minecraft:iron_ingot" },
  "ingredients": [
    { "type": "minecraft:redstone", "quantity": 1, "pos": [ 1, 1, 0 ] },
    { "type": "minecraft:redstone", "quantity": 1, "pos": [ -1, 1, 0 ] },
    { "type": "minecraft:redstone", "quantity": 1, "pos": [ 0, 1, 1 ] },
    { "type": "minecraft:redstone", "quantity": 1, "pos": [ 0, 1, -1 ] }
  ],
  "result": { "id": "minecraft:compass" },
  "effect": "particles",
  "consumeTrigger": true
}
```
Placement (NORTH-facing reference):
```
   Z-
 X- # X+
   Z+
# = origin (clicked stone)
```

## Example 2: Lightning infusion using a mob
Strike a zombie on a copper block with a golden apple to obtain a trident (demo). The zombie must stand directly above the block. The apple isn’t consumed.
```json
{
  "type": "mythicritual:ritual_recipe",
  "target": { "Name": "minecraft:cut_copper" },
  "trigger": { "id": "minecraft:golden_apple" },
  "ingredients": [
    { "type": "minecraft:zombie", "pos": [ 0, 1, 0 ] }
  ],
  "result": { "id": "minecraft:trident" },
  "effect": "lightning",
  "needSky": true,
  "dimensions": [ "minecraft:overworld" ],
  "consumeTrigger": false
}
```
Behavior: Zombie is killed, lightning visual spawns (no real damage), trident item entity appears hovering above the block.

## Performing a ritual (in game)
1. Place or locate the target block.
2. Arrange item entities (drop items) and/or mobs at the required rotated offsets (same relative layout to your facing as in the NORTH reference layout).
3. Hold the trigger item and right‑click the target block with main hand.
4. If matched: inputs consumed, effect plays, result appears as an item entity with a small upward motion.

## Effects
- particles: Anvil sound + radial crit particles.
- lightning: Visual-only lightning bolt (no fire/damage) at one block above origin.
- none: Only (default) anvil sound (suppressed if overridden inside effect implementation later).

## Consumption rules
- Item ingredient: Shrinks the underlying ItemEntity stack by `quantity` (aggregated if reused by multiple ingredients of same stack).
- Mob ingredient: Kills the entity (only one per ingredient). A mob entity cannot satisfy multiple ingredients.
- Trigger: Damaged (if damageable) or removed if `consumeTrigger` is true and player not in Creative.

## Rotation details
Offsets are specified for NORTH. At runtime they rotate: 
- Facing SOUTH: (x,z) -> (-x,-z)
- Facing WEST: (x,z) -> (z,-x)
- Facing EAST: (x,z) -> (-z,x)
Y is unchanged.

## Building & installing
Development:
```
./gradlew build
```
Resulting mod JAR: `build/libs/` (exclude sources classifier). Drop into your `mods` folder on a NeoForge 1.21.1 instance.

## Troubleshooting
- Nothing happens: Ensure you used main hand, server side (singleplayer world counts), offsets correct after rotation, sufficient item counts, sky visibility if required.
- Wrong block: BlockState must match exactly (variants like orientation / properties matter).
- Multiple similar stacks: The system picks the first valid one; avoid ambiguous layouts.

## Extensibility / TODO
- Block / fluid ingredients
- NBT / tag based item ingredient matching
- Custom effects & scripting hooks
- JEI / EMI integration for recipe display

## License
See LICENSE.txt. Recipes you author remain your own unless stated otherwise.
