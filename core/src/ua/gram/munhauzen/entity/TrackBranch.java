package ua.gram.munhauzen.entity;

public class TrackBranch extends TrackMedia {

    public int duration;
    public String option;
    public String optionType;
    public boolean isBeforeNewScenario;
    public boolean containsDayAction;

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return super.toString() + " " + optionType;
    }

    public boolean isFinal() {
        return Option.DEATH.equals(option) || Option.VICTORY.equals(option);
    }

    public boolean isInteraction() {
        return Option.TYPE_INTERACTION.equals(optionType);
    }
}
