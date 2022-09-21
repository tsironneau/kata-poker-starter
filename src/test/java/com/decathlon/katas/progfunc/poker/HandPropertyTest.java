package com.decathlon.katas.progfunc.poker;

import net.jqwik.api.*;
import net.jqwik.api.constraints.Size;
import net.jqwik.api.constraints.UseType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class HandPropertyTest {

    /*
    1 - constructeur valide
    2 - On ajoute la contrainte des 5 cartes -> Test valide
    3 - Test de l'exception
     */
    @Property
    void constructor_list_does_not_have_five_cards(@ForAll @Size(value = 5) List<@UseType Card> cards) {
        //Given
        //When
        Hand hand = new Hand(cards);
        //Then
        assertThat(hand.cards()).containsAll(cards);
    }

    @Property
    void constructor_throws_exception_when_given_list_does_not_have_five_cards(
            @ForAll("card_list_with_size_different_from_five") List<Card> cards) {
        //Given
        //When
        //Then
        assertThatIllegalArgumentException().isThrownBy(() ->
                new Hand(cards)
        );
    }

    @Provide
    Arbitrary<List<Card>> card_list_with_size_different_from_five() {
        return Arbitraries.forType(Card.class)
                .list()
                .filter(l -> l.size() != 5);
    }

}
