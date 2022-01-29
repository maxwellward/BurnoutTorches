package io.github.bananafalls.burnouttorches;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class InitTorches {
    BurnoutTorches instance;
    public InitTorches(BurnoutTorches instance) {
        this.instance = instance;
        pullTorches();
    }

    private void pullTorches() {
        FileConfiguration torchesConfig = instance.getTorchesConfig();
        List<String> torches = torchesConfig.getStringList("torches");

        for(String torchString : torches) {
            Location loc = instance.getDeserializeLocation().deserializeLoc(torchString);
            instance.getTorchManager().StartBurnoutTimer(loc);
        }

    }
}
