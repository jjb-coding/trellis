package com.github.jjbcoding.trellis.example.app;

import com.github.jjbcoding.trellis.service.contributions.defaults.title.TitleContributionAggregator;
import com.github.jjbcoding.trellis.example.enums.injectables.*;
import com.github.jjbcoding.trellis.example.enums.states.*;
import com.github.jjbcoding.trellis.example.nodes.AppNode;
import com.github.jjbcoding.trellis.service.AppService;
import com.github.jjbcoding.trellis.service.AppServiceConfigurationBuilder;

@SuppressWarnings("unused")
public class Builder {
    public Builder() {
        AppServiceConfigurationBuilder builder =
            new AppServiceConfigurationBuilder()
            .setContributionAggregator(new TitleContributionAggregator(" | "))

            .setStateReader(new StateReader(), States.class)
            .setInjectableReader(new InjectableReader(), Injectables.class)

            .setNodesFromReader()

            .setStartupRoot(AppNode.class)
            .setStartupState(States.LOG_IN);

        AppService appService = AppServiceSingleton.INSTANCE.get();
        appService.configure(builder);
        appService.start();
    }
}
