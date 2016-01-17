package utils;

import com.badlogic.gdx.graphics.g2d.Sprite;
import entities.DiscoveredElement;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rezvoi on 14.01.2016.
 */
public class Combinations {

    private static Map<DiscoveredElement, String[]> combinations = new HashMap<DiscoveredElement, String[]>();

    public static void setCombinations() {
        String[] gasString = new String[]{"Heat", "Rock"};
        combinations.put(new DiscoveredElement(new Sprite(Assets.gas), "Gas"), gasString);
        String[] atmosString = new String[]{"Gas", "Gravity"};
        combinations.put(new DiscoveredElement(new Sprite(Assets.atmosphere), "Atmosphere"), atmosString);
        String[] waterString = new String[]{"Cold", "Atmosphere"};
        combinations.put(new DiscoveredElement(new Sprite(Assets.water), "Water"), waterString);
        String[] slimeString = new String[]{"Water", "Heat"};
        combinations.put(new DiscoveredElement(new Sprite(Assets.slime), "Slime"), slimeString);
        String[] plantsString = new String[]{"Slime", "Rock"};
        combinations.put(new DiscoveredElement(new Sprite(Assets.plants), "Plants"), plantsString);
        String[] mountainsString = new String[]{"Rock", "Water"};
        combinations.put(new DiscoveredElement(new Sprite(Assets.mountains), "Mountains"), mountainsString);
        String[] treeString = new String[]{"Plants", "Water"};
        combinations.put(new DiscoveredElement(new Sprite(Assets.tree), "Tree"), treeString);
        String[] oreString = new String[]{"Mountains", "Heat"};
        combinations.put(new DiscoveredElement(new Sprite(Assets.ore), "Ore"), oreString);
        String[] metalString = new String[]{"Ore", "Heat"};
        combinations.put(new DiscoveredElement(new Sprite(Assets.metal), "Metal"), metalString);
        String[] toolString = new String[]{"Metal", "Tree"};
        combinations.put(new DiscoveredElement(new Sprite(Assets.tool), "Tool"), toolString);
        String[] hutString = new String[]{"Tool", "Tree"};
        combinations.put(new DiscoveredElement(new Sprite(Assets.hut), "Hut"), hutString);
        String[] hearthString = new String[]{"Tree", "Heat"};
        combinations.put(new DiscoveredElement(new Sprite(Assets.hearth), "Hearth"), hearthString);
        String[] organismString = new String[]{"Slime", "Water"};
        combinations.put(new DiscoveredElement(new Sprite(Assets.organism), "Organism"), organismString);
        String[] animalString = new String[]{"Organism", "Rock"};
        combinations.put(new DiscoveredElement(new Sprite(Assets.animal), "Animal"), animalString);
        String[] foodString = new String[]{"Animal", "Hearth"};
        combinations.put(new DiscoveredElement(new Sprite(Assets.food), "Food"), foodString);
    }

    public static DiscoveredElement combineElements(DiscoveredElement firstElement, DiscoveredElement secondElement) {

        DiscoveredElement newElement = null;

        for (Map.Entry<DiscoveredElement, String[]> entry : combinations.entrySet()) {
            if (firstElement != null && secondElement != null) {
                if (entry.getValue()[0] == firstElement.getName() && entry.getValue()[1] == secondElement.getName() || entry.getValue()[1] == firstElement.getName() && entry.getValue()[0] == secondElement.getName()) {
                    newElement = entry.getKey();
                    combinations.remove(entry.getKey());
                    break;
                }
            }
        }

        return newElement;

    }

}
