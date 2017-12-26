package id.co.kamil.icalorie;

/**
 * Created by Irham on 12/26/2017.
 */

public class NutritionR {
    private String parentName,parentBerat,childName,childBerat;

    @Override
    public String toString() {
        return "NutritionR{" +
                "parentName='" + parentName + '\'' +
                ", parentBerat='" + parentBerat + '\'' +
                ", childName='" + childName + '\'' +
                ", childBerat='" + childBerat + '\'' +
                '}';
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentBerat() {
        return parentBerat;
    }

    public void setParentBerat(String parentBerat) {
        this.parentBerat = parentBerat;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildBerat() {
        return childBerat;
    }

    public void setChildBerat(String childBerat) {
        this.childBerat = childBerat;
    }
}
