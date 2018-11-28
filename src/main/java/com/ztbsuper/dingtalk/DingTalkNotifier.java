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
import ren.wizard.dingtalkclient.message.Message;

import javax.annotation.Nonnull;
import java.io.IOException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author uyangjie
 */
public class DingTalkNotifier extends Notifier implements SimpleBuildStep {

    private String accessToken;
    private String text;
    private String atMobiles;

    @DataBoundConstructor
    public DingTalkNotifier(String accessToken, String text, String atMobiles) {
        this.accessToken = accessToken;
        this.text = text;
        this.atMobiles = atMobiles;
    }

    public String getAccessToken() {
        return accessToken;
    }
    public String getText() {
        return text;
    }
    public String getAtMobiles() {
        return atMobiles;
    }
    
    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath filePath, @Nonnull Launcher launcher, @Nonnull TaskListener taskListener) throws InterruptedException, IOException {
        String buildInfo = run.getFullDisplayName();

        String WEBHOOK_TOKEN = "https://oapi.dingtalk.com/robot/send?access_token="+accessToken;
    
        HttpClient httpclient = HttpClients.createDefault();
    
        HttpPost httppost = new HttpPost(WEBHOOK_TOKEN);
        httppost.addHeader("Content-Type", "application/json; charset=utf-8");

        // String[] mobiles = atMobiles.split("\\|");
        // String atMobilesStr = "";

        // if (mobiles.length > 1){
        //     for (int i = 0 ; i <mobiles.length - 1 ; i++ ) {
        //         atMobilesStr = atMobilesStr + mobiles[i] + ","; 
        //     } 
        //     atMobilesStr = atMobilesStr + mobiles[mobiles.length - 1]
        // } else {
            
        // }
        
        String textMsg = "{ \"msgtype\": \"markdown\", \"markdown\": {\"title\": \""+buildInfo+"\", \"text\": \""+text+"\" }, \"at\": {\"atMobiles\": [\""+ atMobiles +"\"],\"isAtAll\": false }}";

        StringEntity se = new StringEntity(textMsg, "utf-8");
        httppost.setEntity(se);
    
        HttpResponse response = httpclient.execute(httppost);
        if (response.getStatusLine().getStatusCode()== HttpStatus.SC_OK){
            String result= EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println(result);
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
