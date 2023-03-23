package io.github.guy7cc.wwrpg.util;

import com.mojang.datafixers.util.Pair;

import java.util.*;

public class IntRange {
    // (0-indexed) The even-indexed numbers are inclusive start of range, and the odd-indexed numbers are exclusive end of range.
    private List<Integer> list;

    private int size;
    private List<Pair<Integer, Integer>> indexToValue;

    private IntRange(List<Integer> list){
        if(list.size() % 2 == 1) throw new IllegalArgumentException("List that is private field of IntRange must have even size.");
        this.list = list;
        size = 0;
        indexToValue = new ArrayList<>();
        for(int i = 0; i < list.size(); i += 2){
            indexToValue.add(Pair.of(size, list.get(i)));
            size += list.get(i + 1) - list.get(i);
        }
    }

    public static IntRange simple(int value){
        return simple(value, value);
    }

    public static IntRange simple(int start, int end){
        if(start > end){
            int tmp = start;
            start = end;
            end = tmp;
        }
        end++;
        return new IntRange(List.of(start, end));
    }

    public static IntRange fromString(String str) throws IllegalArgumentException{
        Builder builder = new Builder();
        String[] array = str.replaceAll(" ", "").split(",");
        for(String s : array){
            String[] a = s.split(":");
            if(a.length == 1){
                int value = Integer.parseInt(a[0]);
                builder.add(value);
            } else if(a.length == 2){
                int start = Integer.parseInt(a[0]);
                int end = Integer.parseInt(a[1]);
                builder.add(start, end);
            } else throw new IllegalArgumentException(s + " is an illegal expression for IntRange.");
        }
        return builder.build();
    }

    public boolean contains(int value){
        int index = Collections.binarySearch(list, value);
        return index % 2 == 0;
    }

    public int size(){
        return size;
    }

    public boolean isEmpty(){
        return size == 0;
    }

    public int get(int index){
        if(index < 0 && size <= index) throw new IndexOutOfBoundsException();
        int i = Collections.binarySearch(indexToValue, Pair.of(index, 0), Comparator.comparingInt(Pair::getFirst));
        if(i >= 0) return indexToValue.get(i).getSecond();
        else{
            i = -2 - i;
            return index - indexToValue.get(i).getFirst() + indexToValue.get(i).getSecond();
        }
    }

    public int indexOf(int value){
        if(!contains(value)) throw new IllegalArgumentException("The specified value is not contained in IntRange.");
        int i = Collections.binarySearch(indexToValue, Pair.of(0, value), Comparator.comparingInt(Pair::getSecond));
        if(i >= 0) return indexToValue.get(i).getFirst();
        else {
            i = -2 - i;
            return value - indexToValue.get(i).getSecond() + indexToValue.get(i).getFirst();
        }
    }

    public int min(){
        if(isEmpty()) throw new IllegalStateException("IntRange is empty.");
        return list.get(0);
    }

    public int max(){
        if(isEmpty()) throw new IllegalStateException("IntRange is empty.");
        return list.get(list.size() - 1) - 1;
    }

    public int floor(int value){
        if(isEmpty()) throw new IllegalStateException("IntRange is empty.");
        if(value > max()) throw new IllegalArgumentException("The specified value is more than maximum number of IntRange.");
        int i = Collections.binarySearch(list, value);
        if(i >= 0) return list.get((i + 1) / 2 * 2);
        else return i % 2 == 0 ? value : list.get(-1 - i);
    }

    public int ceil(int value){
        if(isEmpty()) throw new IllegalStateException("IntRange is empty.");
        if(value < min()) throw new IllegalArgumentException("The specified value is less than minimum number of IntRange.");
        int i = Collections.binarySearch(list, value);
        if(i >= 0) return i % 2 == 0 ? list.get(i) : list.get(i) - 1;
        else return i % 2 == 0 ? value : list.get(-2 - i) - 1;
    }

    @Override
    public String toString(){
        if(list.size() == 0) return "";
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < list.size(); i += 2){
            if(i > 0) sb.append(',');
            if(list.get(i) == list.get(i + 1) - 1) sb.append(list.get(i));
            else sb.append(list.get(i)).append(':').append(list.get(i + 1) - 1);
        }
        return sb.toString();
    }

    public static class Builder {
        protected List<Pair<Integer, Boolean>> components = new ArrayList<>();

        public Builder add(int value){
            return add(value, value);
        }

        public Builder add(int start, int end){
            if(start > end) {
                int tmp = start;
                start = end;
                end = tmp;
            }
            end++;
            components.add(Pair.of(start, true));
            components.add(Pair.of(end, false));
            return this;
        }

        public IntRange build(){
            components.sort(Comparator.comparingInt(Pair::getFirst));
            List<Integer> list = new ArrayList<>();
            int a = 0;
            for(Pair<Integer, Boolean> c : components){
                if(c.getSecond() && a++ == 0 || !c.getSecond() && --a == 0) list.add(c.getFirst());
            }
            return new IntRange(list);
        }

    }
}
