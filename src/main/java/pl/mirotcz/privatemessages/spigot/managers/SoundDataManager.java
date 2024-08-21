package pl.mirotcz.privatemessages.spigot.managers;

import org.bukkit.Sound;
import pl.mirotcz.privatemessages.spigot.PrivateMessages;

import java.util.ArrayList;
import java.util.List;

public class SoundDataManager {
    private List<SoundData> soundsData = new ArrayList<>();

    public void load() {
        this.soundsData = new ArrayList<>();
        ConfigManager config = PrivateMessages.get().getManagers().getConfigManagers().getSoundsConfigManager();

        for (String number : config.getConfig().getKeys(false)) {
            Sound sound = Sound.valueOf(config.getConfig().getString(number + ".SoundName"));
            float volume = (float) config.getConfig().getDouble(number + ".Volume");
            float pitch = (float) config.getConfig().getDouble(number + ".Pitch");
            this.soundsData.add(new SoundData(sound, pitch, volume));
        }

    }

    public SoundData getData(int number) {
        return this.soundsData.size() >= number ? this.soundsData.get(number - 1) : null;
    }

    public List<SoundData> getAllData() {
        return this.soundsData;
    }
}
