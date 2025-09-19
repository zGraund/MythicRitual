# Mythic Ritual

**Mythic Ritual** is a [NeoForge](https://neoforged.net/) mod that adds an **in-world crafting system**.  
Instead of using a crafting table, players can perform **rituals** by right-clicking an altar block with a catalyst item.

Recipes can be defined via **datapacks** or **code** (using the provided `RitualRecipeBuilder` in datagen).

>⚠️ This mod is currently in beta. Expect frequent updates, potential bugs, and possible breaking changes. Backup your worlds before use and report any issues on GitHub!

##  Features
- Trigger rituals by right-clicking a specified block (`altar`) with an item in hand (`catalyst`).
- Recipes can require **offerings placed in specific world positions** relative to the altar.
- Supports **items, entities, or tags** as ingredients or results.
- Limit rituals to some **dimensions and/or biomes**.
- Configurable **visual effects** (lightning, particles, none). (WIP)
- Flexible **ritual actions** (consume or keep altar/catalyst, drop or place result, etc.).

## Recipe Format

Recipes are defined in JSON format with the following structure:

```json
{
  "altar": "minecraft:stone",
  "catalyst": {
    "items": "minecraft:wooden_axe"
  },
  "locations": [
    {
      "position": [0, 1, 0],
      "offerings": [
        {
          "items": "minecraft:melon"
        }
      ]
    }
  ],
  "result": {
    "items": "minecraft:melon_slice",
    "count": 8
  },
  "dimensions": [
    "minecraft:overworld"
  ],
  "biomes": [
    "minecraft:forest"
  ],
  "effect": "lightning",
  "actions": [
    "damage_catalyst",
    "drop_result"
  ],
  "sky": true
}
```

### Recipe Fields

| Field        | Type             | Required | Description                                      |
|--------------|------------------|----------|--------------------------------------------------|
| `type`       | string           | ✅        | Always `mythicritual:ritual_recipe`              |
| `altar`      | BlockState       | ✅        | Block to right-click to trigger the ritual       |
| `catalyst`   | RitualIngredient | ❌        | Item in hand required to trigger the recipe      |
| `locations`  | List(*)          | ❌        | Positioned offerings around the altar            |
| `result`     | RitualIngredient | ✅        | The crafting result                              |
| `dimensions` | List             | ❌        | Allowed dimensions                               |
| `biomes`     | List             | ❌        | Allowed biomes or biome tags                     |
| `effect`     | Effect           | ❌        | Visual effect (`lightning`, `particles`, `none`) |
| `actions`    | List             | ❌        | Post-craft actions (see below)                   |
| `sky`        | Boolean          | ❌        | Whether sky access is required                   |
(*) refer to example above for format information.
### Ritual Actions

Configure what happens after a successful ritual:

- `keep_catalyst` - Preserve the catalyst item
- `damage_catalyst` - Damage the catalyst item
- `destroy_catalyst` - Consume the catalyst item
- `keep_altar` - Leave the altar block unchanged
- `destroy_altar` - Remove the altar block
- `drop_result` - Drop the result as an item
- `place_result` - Place the result block in the world

## Ritual Ingredients

The `RitualIngredient` system supports both items and entities:

### Item Ingredients

```json
{
  "items": [
    // Both items will be accepted
    "minecraft:diamond",
    "minecraft:emerald"
  ],
  "components": {...},
  "count": 3
}
```

### Entity Ingredients

```json
{
  "entity": "minecraft:cow"
}
```

## For Modders

### Using the Recipe Builder

The mod provides a `RitualRecipeBuilder` for datagen:

```java
public class MyRecipeProvider extends RecipeProvider {
    // Use RitualRecipeBuilder in your datagen
    // See code documentation for complete API reference
    protected void buildRecipes(RecipeOutput, HolderLookup.Provider) {
        RitualRecipeHelpers.builder(...)
    }
}
```
