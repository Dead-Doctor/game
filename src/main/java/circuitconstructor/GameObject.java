package circuitconstructor;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private String name;
    private List<Component> components;
    public Transform transform;

    public GameObject(String name) {
        init(name, new Transform());
    }

    public GameObject(String name, Transform transform) {
        init(name, transform);
    }

    private void init(String name, Transform transform) {
        this.name = name;
        this.components = new ArrayList<>();
        this.transform = transform;
    }

    public void start() {
        components.forEach(Component::start);
    }

    public void update(float dTime) {
        components.forEach(c -> c.update(dTime));
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component c : components) {
            if (componentClass.isAssignableFrom(c.getClass())) {
                try {
                    return componentClass.cast(c);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Casting component.";
                }
            }
        }
        return null;
    }

    public void addComponent(Component c) {
        components.add(c);
        c.gameObject = this;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if (componentClass.isAssignableFrom(c.getClass())) {
                components.remove(i);
                return;
            }
        }
    }
}
