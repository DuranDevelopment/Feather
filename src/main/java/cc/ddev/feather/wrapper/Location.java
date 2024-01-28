package cc.ddev.feather.wrapper;


import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.instance.Instance;

public class Location {

    private Pos position;

    @Getter
    private Instance instance;

    public Location(Pos position, Instance instance) {
        this.position = position;
        this.instance = instance;
    }
    public Pos getPos() {
        return position;
    }

    public double getX() {
        return position.x();
    }
    public double getY() {
        return position.y();
    }
    public double getZ() {
        return position.z();
    }
}