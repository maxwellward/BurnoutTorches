# BurnoutTorches
A [Spigot/Paper plugin](https://www.spigotmc.org/resources/burnout-torches.77125/) that causes torches to break and drop an item after a set time.

Features:
- All options are customizable
- Refueling torches with any number of customizable items by right-clicking a torch
- Toggleable creative mode bypass
- A '/bt reload' command to reload the config without restarting your server

If you have an issue or have found a bug, please [create an issue](https://github.com/maxwellward/BurnoutTorches/issues/new) and include as much information as possible!

Do not manually edit `torches.yml`, this is used to store existing torches and their remaining burn time when shutting down the server.

Permissions:
- burnouttorches.reload (reload the plugin)

Configuration:
| Option        | Default           | Information  |
| ------------- | ------------- | ------------- |
| time | 60 | The time in seconds for torches to burn out. Decimals are not supported. |
| drop | STICK | The item to drop when a torch burns out. An item list can be found [here](https://papermc.io/javadocs/paper/1.18/org/bukkit/Material.html). |
| alow-refueling | true | If players should be able to refuel torches |
| refuel-type | RESET | If, when refuelling, time should be added to the existing remanining time or if it should be reset to the configured time.
| sound-on-fuel | true | If a fire charge sound should play when refuelling a torch. |
| particle-on-fuel | true | If green particles should show when refuelling a torch. |
| refuel-items | COAL, CHARCOAL | What items are able to be used to refuel torches. |
| burnout-in-creative | false | If torches placed in creative mode should burn out or be refuelable. |
| autosave | true | If torches should periodically be saved to the disk, instead of only at shutdown |
| autosave-frequency | 600 | How often (in seconds) torches should be saved to the disk, if autosaving is enabled. |
