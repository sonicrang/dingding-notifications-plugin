package com.ztbsuper.dingtalk;

import com.ztbsuper.Messages;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;
import jenkins.tasks.SimpleBuildStep;
import org.apache.commons.lang3.StringUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.QueryParameter;
import ren.wizard.dingtalkclient.DingTalkClient;
import ren.wizard.dingtalkclient.message.DingMessage;
import ren.wizard.dingtalkclient.message.LinkMessage;

import javax.annotation.Nonnull;
import java.io.IOException;

/**
 * @author uyangjie
 */
public class DingTalkNotifier extends Notifier implements SimpleBuildStep {

    private String accessToken;
    private String message;
    private String imageUrl;
    private String messageUrl;

    @DataBoundConstructor
    public DingTalkNotifier(String accessToken, String message, String imageUrl, String messageUrl) {
        this.accessToken = accessToken;
        this.message = message;
        this.imageUrl = imageUrl;
        this.messageUrl = messageUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }
    public String getMessage() {
        return message;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getMessageUrl() {
        return messageUrl;
    }
    // @DataBoundSetter
    // public void setAccessToken(String accessToken) {
    //     this.accessToken = accessToken;
    // }

    // public String getNotifyPeople() {
    //     return notifyPeople;
    // }

    // @DataBoundSetter
    // public void setNotifyPeople(String notifyPeople) {
    //     this.notifyPeople = notifyPeople;
    // }

    

    // @DataBoundSetter
    // public void setMessage(String message) {
    //     this.message = message;
    // }

   

    // @DataBoundSetter
    // public void setImageUrl(String imageUrl) {
    //     this.imageUrl = imageUrl;
    // }

    // public String getJenkinsUrl() {
    //     return jenkinsUrl;
    // }

    // @DataBoundSetter
    // public void setJenkinsUrl(String jenkinsUrl) {
    //     this.jenkinsUrl = jenkinsUrl;
    // }

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath filePath, @Nonnull Launcher launcher, @Nonnull TaskListener taskListener) throws InterruptedException, IOException {
        String buildInfo = run.getFullDisplayName();
        if (!StringUtils.isBlank(message)) {
            sendMessage(LinkMessage.builder()
                    .title(buildInfo)
                    .picUrl(imageUrl)
                    .text(message)
                    .messageUrl(messageUrl)
                    .build());
        }
    }

    private void sendMessage(DingMessage message) {
        DingTalkClient dingTalkClient = DingTalkClient.getInstance();
        try {
            dingTalkClient.sendMessage(accessToken, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Symbol("dingTalk")
    @Extension
    public static final class DescriptorImpl extends BuildStepDescriptor<Publisher> {

        // public FormValidation doCheck(@QueryParameter String accessToken, @QueryParameter String notifyPeople) {
        //     if (StringUtils.isBlank(accessToken)) {
        //         return FormValidation.error(Messages.DingTalkNotifier_DescriptorImpl_AccessTokenIsNecessary());
        //     }
        //     return FormValidation.ok();
        // }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            return true;
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return Messages.DingTalkNotifier_DescriptorImpl_DisplayName();
        }
    }
}
