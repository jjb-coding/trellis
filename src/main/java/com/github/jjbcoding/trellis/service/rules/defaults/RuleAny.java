package com.github.jjbcoding.trellis.service.rules.defaults;

import com.github.jjbcoding.trellis.service.rules.IRule;

import java.util.ArrayList;
import java.util.List;

/**
 * A rule which validates successfully if any sub-rule validates.
 */
public class RuleAny<T>
    implements IRule<T> {
    // ----- DYNAMIC
    // *** FIELDS
    List<Rule<T>> rules;

    // *** CONSTRUCTORS
    /**
     * Constructs a RuleAny instance.
     * @param rules     The list of rules
     */
    @SuppressWarnings("unused")
    public RuleAny(List<Rule<T>> rules) {
        this.rules = rules;
    }

    /**
     * Constructs a RuleAny instance.
     */
    @SuppressWarnings("unused")
    public RuleAny() {
        rules = new ArrayList<>();
    }

    // *** METHODS
    /**
     * Adds a rule.
     * @param rule  The rule to add
     * @return      Self-returning
     */
    @SuppressWarnings("unused")
    public RuleAny<T> add(Rule<T> rule) {
        rules.add(rule);
        return this;
    }

    /**
     * Adds many rules.
     * @param rules The rules to add
     * @return      Self-returning
     */
    @SuppressWarnings("unused")
    public RuleAny<T> add(Iterable<Rule<T>> rules) {
        for (Rule<T> rule : rules)
            this.rules.add(rule);
        return this;
    }

    // *** INTERFACE IMPLEMENTATIONS
    // * [ IValidationProvider ]
    @SuppressWarnings("unused")
    @Override
    public boolean isValid(Iterable<Class<? extends T>> discoveredClasses) {
        for (Rule<T> rule : rules)
            if (rule.isValid(discoveredClasses))
                return true;
        return false;
    }
}
