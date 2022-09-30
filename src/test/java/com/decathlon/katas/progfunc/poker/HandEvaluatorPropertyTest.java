package com.decathlon.katas.progfunc.poker;

import net.jqwik.api.*;
import net.jqwik.api.arbitraries.ListArbitrary;
import net.jqwik.api.constraints.UseType;
import net.jqwik.api.lifecycle.BeforeProperty;

import static org.assertj.core.api.Assertions.assertThat;

/*
Trouver et écrire des propriétés, et voir si on peut découvrir des contre-exemples
 */
public class HandEvaluatorPropertyTest {

    private HandEvaluator cut;

    @BeforeProperty
    void setup() {
        cut = new HandEvaluator();
    }

    @Property
    void a_hand_can_not_has_pair_and_double_pair(@ForAll @UseType Hand hand) {
        //When
        boolean isPair = cut.isPair(hand);
        boolean doublePair = cut.isDoublePair(hand);
        //Then
        assertThat(isPair && doublePair).isFalse();
    }

    @Property
    void check_that_min_and_max_rank_is_correctly_ordered_by_min(@ForAll @UseType Hand hand) {
        //When
        Rank minRank = cut.minRankCardBy(hand, Rank::getMin);
        Rank maxRank = cut.maxRankCardBy(hand, Rank::getMin);
        //Then
        assertThat(minRank.getMin()).isLessThanOrEqualTo(maxRank.getMin());
    }

    @Property
    void check_that_min_and_max_rank_is_correctly_ordered_by_max(@ForAll @UseType Hand hand) {
        //When
        Rank minRank = cut.minRankCardBy(hand, Rank::getMax);
        Rank maxRank = cut.maxRankCardBy(hand, Rank::getMax);
        //Then
        assertThat(minRank.getMax()).isLessThanOrEqualTo(maxRank.getMax());
    }

    @Property
    void check_flush_hand(@ForAll("flushHand") Hand hand) {
        //When
        boolean expected = cut.hasFlush(hand);
        //Then
        assertThat(expected).isTrue();
    }

    @Provide
    Arbitrary<Hand> flushHand() {
        Arbitrary<Color> colorArbitrary = Arbitraries.of(Color.class);
        ListArbitrary<Rank> ranksArbitrary = Arbitraries.of(Rank.class).list().ofSize(5);
        return Combinators.combine(colorArbitrary, ranksArbitrary)
                .as((color, ranks) -> ranks.stream()
                        .map(rank -> new Card(rank, color))
                        .toList())
                .map(Hand::new);
    }

/*    @Provide
    Arbitrary<Hand> nOfAKind(){
        ListArbitrary<Color> colorsArbitrary = Arbitraries.of(Color.class).list().ofSize(5);
        ListArbitrary<Rank> ranksArbitrary = Arbitraries.of(Rank.class)
                .list()
                .ofSize(4)
                .uniqueElements();
        return Combinators.combine(colorsArbitrary, ranksArbitrary)
                .flatAs()

    }*/
}
