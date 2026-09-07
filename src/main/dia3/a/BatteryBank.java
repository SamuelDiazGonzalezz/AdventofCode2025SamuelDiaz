package software.ulpgc.day03.a;

import java.util.List;

public record BatteryBank(List<Integer> batteries)  {

    public int sum() {
        if (batteries.isEmpty()) return 0;
        if (batteries.size() == 1) return batteries.getFirst();
        if (batteries.size() == 2) return joinChars(batteries.getFirst(), batteries.getLast());

        return joinChars(
                getMaxNum(batteries.subList(0, batteries.size()-1)),
                getMaxNum(batteries.subList(batteries.indexOf(
                        getMaxNum(
                                batteries.subList(0, batteries.size()-1)))+1,
                                batteries.size()
                        )
                )
        );
    }

    private int getMaxNum(List<Integer> batteries) {
        if  (batteries.isEmpty()) return 0;
        return batteries.stream().max(Integer::compare).get();
    }

    private int joinChars(Integer integer, Integer integer1) {
        return Integer.parseInt(integer.toString() + integer1.toString());
    }
}
