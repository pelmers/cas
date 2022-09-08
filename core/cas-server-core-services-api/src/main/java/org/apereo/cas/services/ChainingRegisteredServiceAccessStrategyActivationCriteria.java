package org.apereo.cas.services;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * This is {@link ChainingRegisteredServiceAccessStrategyActivationCriteria}.
 *
 * @author Misagh Moayyed
 * @since 6.5.0
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@JsonIgnoreProperties("order")
public class ChainingRegisteredServiceAccessStrategyActivationCriteria implements RegisteredServiceAccessStrategyActivationCriteria {
    @Serial
    private static final long serialVersionUID = 5118603912161923218L;

    private List<RegisteredServiceAccessStrategyActivationCriteria> conditions = new ArrayList<>();

    private RegisteredServiceChainOperatorTypes operator = RegisteredServiceChainOperatorTypes.AND;

    /**
     * Add policy/strategy.
     *
     * @param policy the provider
     */
    public void addCondition(final @NonNull RegisteredServiceAccessStrategyActivationCriteria policy) {
        conditions.add(policy);
    }

    /**
     * Add conditions.
     *
     * @param policies the policies
     */
    public void addConditions(final RegisteredServiceAccessStrategyActivationCriteria... policies) {
        Arrays.stream(policies).forEach(this::addCondition);
    }

    @Override
    public boolean shouldActivate(final RegisteredServiceAccessStrategyRequest request) {
        if (operator == RegisteredServiceChainOperatorTypes.OR) {
            return conditions.stream()
                .sorted(Comparator.comparing(RegisteredServiceAccessStrategyActivationCriteria::getOrder))
                .anyMatch(condition -> condition.shouldActivate(request));
        }
        return conditions.stream()
            .sorted(Comparator.comparing(RegisteredServiceAccessStrategyActivationCriteria::getOrder))
            .allMatch(condition -> condition.shouldActivate(request));
    }

    @Override
    public boolean shouldAllowIfInactive() {
        if (operator == RegisteredServiceChainOperatorTypes.OR) {
            return conditions.stream()
                .sorted(Comparator.comparing(RegisteredServiceAccessStrategyActivationCriteria::getOrder))
                .anyMatch(RegisteredServiceAccessStrategyActivationCriteria::shouldAllowIfInactive);
        }
        return conditions.stream()
            .sorted(Comparator.comparing(RegisteredServiceAccessStrategyActivationCriteria::getOrder))
            .allMatch(RegisteredServiceAccessStrategyActivationCriteria::shouldAllowIfInactive);
    }
}
