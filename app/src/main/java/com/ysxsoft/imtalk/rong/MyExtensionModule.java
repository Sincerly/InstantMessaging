package com.ysxsoft.imtalk.rong;

import java.util.List;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.ImagePlugin;
import io.rong.imkit.widget.provider.FilePlugin;
import io.rong.imlib.model.Conversation;

/**
 * Create By 胡
 * on 2019/8/15 0015
 */
public class MyExtensionModule extends DefaultExtensionModule {
    private MyPlugin myPlugin = new MyPlugin();
    private GiftPlugin giftPlugin = new GiftPlugin();
    private ZBPlugin zbPlugin = new ZBPlugin();
    private ImagePlugin imgPlugin = new ImagePlugin();
    private FilePlugin filePlugin = new FilePlugin();


    @Override
    public List<IPluginModule> getPluginModules(Conversation.ConversationType conversationType) {
        List<IPluginModule> pluginModules = super.getPluginModules(conversationType);
        if (pluginModules.size()>0) {
            pluginModules.clear();
        }
        pluginModules.add(myPlugin);
        pluginModules.add(giftPlugin);
        pluginModules.add(zbPlugin);
        return pluginModules;
    }
}
