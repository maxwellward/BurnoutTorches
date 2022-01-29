package io.github.bananafalls.burnouttorches;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            HashMap<Location, Long> torch = instance.getDeserializeTorch().deserializeTorch(torchString);
            for(Map.Entry<Location, Long> entry : torch.entrySet()) {
                long remaining = (entry.getValue() / 1000);
                instance.getTorchManager().StartBurnoutTimer(entry.getKey(), remaining);
                System.out.println("Continued old torch with " + remaining + " seconds remaining (" + remaining * 20 + "t)");
            }
        }
    }
}
