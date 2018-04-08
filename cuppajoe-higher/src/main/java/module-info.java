module com.github.bishabosha.cuppajoe.higher {
    requires transitive com.github.bishabosha.cuppajoe.annotation;

    exports com.github.bishabosha.cuppajoe.higher.functions;
    exports com.github.bishabosha.cuppajoe.higher.applicative;
    exports com.github.bishabosha.cuppajoe.higher.compose;
    exports com.github.bishabosha.cuppajoe.higher.foldable;
    exports com.github.bishabosha.cuppajoe.higher.functor;
    exports com.github.bishabosha.cuppajoe.higher.monad;
    exports com.github.bishabosha.cuppajoe.higher.monoid;
    exports com.github.bishabosha.cuppajoe.higher.peek;
    exports com.github.bishabosha.cuppajoe.higher.value;
    exports com.github.bishabosha.cuppajoe.higher.unapply;
}