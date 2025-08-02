package onCommand;
import org.bukkit.permissions.Permission;

public class Permissions {
    public static final Permission ADMIN = new Permission(
            "pitperks.admin",
            "access to global perk management"
    );
    public static final Permission GOLDEN_HEADS = new Permission(
            "perks.golden_heads",
            "Max health increased by 2 hearts."
    );
    public static final Permission FISHING_ROD = new Permission(
            "perks.fishing_rod",
            "Spawn with a fishing rod good for pvp and fishing."
    );
    public static final Permission LAVA = new Permission(
            "perks.lava",
            "Spawn in with a lava bucket good for pvp."
    );
    public static final Permission STRENGTH_CHAINING = new Permission(
            "pitperks.strength_chaining",
            "8% stacking strength buff on kill."
    );
    public static final Permission JUGGERNAUT = new Permission(
            "perks.juggernaut",
            "Spawn in with the Juggernaut Helmet reducing damage and knockback"
    );
    public static final Permission BARBARIAN = new Permission(
            "perks.barbarian",
            "Spawn with axes instead of swords. Axes deal 0.5 more damage."
    );
    public static final Permission RING_MINER = new Permission(
            "perks.ring_miner",
            "Spawn with axes instead of swords. Axes deal 0.5 more damage."
    );
    public static final Permission BONK = new Permission(
            "perks.bonk",
            "First hit recieved from a player gets deflected off and they can't hurt you for 1 second."
    );
    public static final Permission TRICKLE_DOWN = new Permission(
            "perks.trickle_down",
            "Spawn with axes instead of swords. Axes deal 0.5 more damage."
    );
    public static final Permission LUCKY_DIAMOND = new Permission(
            "perks.lucky_diamond",
            "Spawn with axes instead of swords. Axes deal 0.5 more damage."
    );
    public static final Permission SPAMMER = new Permission(
            "perks.spammer",
            "Spawn with axes instead of swords. Axes deal 0.5 more damage."
    );
    public static final Permission BOUNTY_HUNTER = new Permission(
            "perks.bounty_hunter",
            "Spawn with bounty hunter leggings. Wearing the leggings gives you a 1% damage buff against bounties per 100g bounty."
    );
    public static final Permission STREAKER = new Permission(
            "perks.streaker",
            "3x kill-streak xp."
    );
    public static final Permission SUPER_STREAKER = new Permission(
            "perks.super_streaker",
            "Dieing on a megastreak prevents the loss of mystic lives. Assists count their damage towards your killstreak."
    );
    public static final Permission CO_OP_CAT = new Permission(
            "perks.co_op_cat",
            "Assists give a minimum of 80% the kill rewards instead of using damage contribution. Assists have a 20% chance to be treated like a kill by other perks."
    );
    public static final Permission CONGLOMORATE = new Permission(
            "perks.conglomorate",
            "All of the experience you earn is converted into gold at a 25% ratio"
    );
    public static final Permission GLADIATOR = new Permission(
            "perks.gladiator",
            "Reduces damage by 3% per player in a 12-block radius, up to 39%"
    );
    public static final Permission VAMPIRISM = new Permission(
            "perks.vampirism",
            "Every hit heals 0.5 health and kills give regeneration 1 (0:08)"
    );
    public static final Permission RECON = new Permission(
            "perks.recon",
            "40 experience every fourth bow shot on any players."
    );
    public static final Permission OVERHEAL = new Permission(
            "perks.overheal",
            "Double healing item limits"
    );
    public static final Permission RAMBO = new Permission(
            "perks.rambo",
            "Max-Health is reduced by 2 hearts. Health is fully refilled on kill."
    );
    public static final Permission OLYMPUS = new Permission(
            "perks.olympus",
            "Olympus potions"
    );
    public static final Permission DIRTY = new Permission(
            "perks.dirty",
            "Kills give resistance II (0:04)."
    );
    public static final Permission FIRST_STRIKE = new Permission(
            "perks.first_strike",
            "First hit on a player grants speed I (0:10) and +35% damage."
    );
    public static final Permission TASTY_SOUPS = new Permission(
            "perks.soups",
            "Instead of gaining golden apples on kill gain tasty soups."
    );
    public static final Permission MARATHON = new Permission(
            "perks.marathon",
            "Cant wear boots, perma speed II, +33% damage and +10% resistance."
    );
    public static final Permission THICK = new Permission(
            "perks.thick",
            "Max health increased by 2 hearts."
    );
    public static final Permission GOLDEN_STEAKS = new Permission(
            "perks.golden_steaks",
            "steak it until you make it. steaks give yummy tastes and buffs."
    );

}
