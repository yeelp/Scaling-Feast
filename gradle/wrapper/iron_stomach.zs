import crafttweaker.event.PlayerTickEvent;
import crafttweaker.potions.IPotion;
import crafttweaker.player.IPlayer;

import mods.compatskills.TraitCreator;

// TRAIT CREATION

var iron = TraitCreator.createTrait("iron_stomach", 2, 0, "reskillable:defense", 3, 
    "reskillable:defence|3", "compatskills:cooking|12");

iron.name = "Iron Stomach";
iron.description = "You are now immune to Hunger and thirst debuffs";

// TRAIT FUNCTIONALITY
iron.onPlayerTick = function(event as PlayerTickEvent) {
    if (!iron.getEnabled())
        return;

    val player = event.player;
    val effects = player.activePotionEffects;

    val thirst = <potion:simpledifficulty:thirst>;
    val parasites = <potion:simpledifficulty:parasites>;
    val hunger = <potion:minecraft:hunger>;

    if (effects == null)
        return;

    for potion in effects {
        if (potion != null) {
            val name = potion.effectName;

            if (name == thirst.name)
                player.removePotionEffect(thirst);

            if (name == parasites.name)
                player.removePotionEffect(parasites);

            if (name == hunger.name)
                player.removePotionEffect(hunger);
        }
    }
};