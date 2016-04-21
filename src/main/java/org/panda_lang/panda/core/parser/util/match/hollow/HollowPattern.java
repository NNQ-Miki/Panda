package org.panda_lang.panda.core.parser.util.match.hollow;

import org.panda_lang.panda.core.parser.util.match.Matcher;

import java.util.ArrayList;
import java.util.List;

public class HollowPattern implements Matcher {

    private final int index;
    private final List<HollowSymbol> hollowSymbols;
    private final List<String> hollows;

    protected HollowPattern(int index, List<HollowSymbol> hollowSymbols) {
        this.index = index;
        this.hollowSymbols = hollowSymbols;
        this.hollows = new ArrayList<>();
    }

    public static HollowPatternBuilder builder() {
        return new HollowPatternBuilder();
    }

    @Override
    public boolean match(String expression) {
        hollows.clear();

        for (int f = 0; f < hollowSymbols.size(); f++) {
            HollowSymbol hollowSymbol = hollowSymbols.get(f);

            loop:
            if (hollowSymbol.isBasis()) {

                for (String variant : hollowSymbol.getVariants()) {
                    String part = getPart(expression, 0, variant.length());

                    if (variant.equals(part)) {
                        expression = expression.substring(variant.length(), expression.length());
                        break loop;
                    }
                }

                return false;
            }
            else if (hollowSymbol.isHollow()) {
                HollowSymbol nextHollowSymbol = hollowSymbols.size() > hollowSymbol.getIndex() + 1 ? hollowSymbols.get(hollowSymbol.getIndex() + 1) : hollowSymbol;

                for (String variant : nextHollowSymbol.getVariants()) {
                    int partIndex = expression.indexOf(variant);

                    if (partIndex < 0) {
                        return false;
                    }

                    String hollow = expression.substring(0, partIndex);
                    expression = expression.substring(partIndex, expression.length());

                    hollows.add(hollow);
                    break loop;
                }

                hollows.add(expression);
            }

        }

        return true;

        /*List<String> fragments = HollowPatternUtils.toFragments(expression);
        int f = 0;

        hollows.clear();

        for (HollowSymbol hollowSymbol : hollowSymbols) {
            loop:

            if (hollowSymbol.isBasis()) {
                if (f >= fragments.size()) {
                    return false;
                }

                String fragment = fragments.get(f);

                if (hollowSymbol.match(fragment)) {
                    f++;
                    break loop;
                }
                return false;
            }

            else if (hollowSymbol.isHollow()) {
                HollowSymbol nextHollowSymbol = hollowSymbols.size() > hollowSymbol.getIndex() + 1 ? hollowSymbols.get(hollowSymbol.getIndex() + 1) : hollowSymbol;
                StringBuilder hollowBuilder = new StringBuilder();

                for (int i = f; i < fragments.size(); i++) {
                    String nextFragment = fragments.get(i);

                    if (!nextHollowSymbol.isHollow() && nextHollowSymbol.match(nextFragment)) {
                        hollows.add(hollowBuilder.toString().trim());
                        break loop;
                    }

                    hollowBuilder.append(nextFragment);
                    hollowBuilder.append(" ");
                    f++;
                }

                hollows.add(hollowBuilder.toString().trim());
            }

            // <todo: optional>

        }
        return true;
        */
    }

    public String getPart(String string, int beginIndex, int endIndex) {
        return string.length() >= endIndex ? string.substring(beginIndex, endIndex) : string.substring(beginIndex, string.length());
    }

    public List<String> getHollows() {
        return hollows;
    }

    public List<HollowSymbol> getHollowSymbols() {
        return hollowSymbols;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public String toString() {
        return hollowSymbols.toString();
    }

}
