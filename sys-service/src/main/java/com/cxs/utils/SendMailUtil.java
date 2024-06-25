package com.cxs.utils;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.Resource;
import com.cxs.config.MailConfig;
import com.cxs.enums.CurrencyErrorEnum;
import com.cxs.exception.CurrencyException;
import com.cxs.model.Feedback;
import com.cxs.model.FeedbackReply;
import com.cxs.model.Report;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.MessageFormat;


@Slf4j
@Component
public class SendMailUtil {
    @Autowired
    private JavaMailSenderImpl javaMailSender;

    @Autowired
    private MailConfig mailConfig;

    /**
     * 发送带附近的邮件信息
     * @param to 收件人
     * @param subject  主题
     * @param content  内容
     */
    public void sendMail(String to, String subject, String content) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setTo(to);
            helper.setFrom(mailConfig.getTitle() + "中心<" + mailConfig.getUsername() + ">");
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new CurrencyException(CurrencyErrorEnum.SYSTEM_ERROR);
        }

    }


    /**
     * 读取邮件模板
     * 替换模板中的信息
     * @param code 内容
     * @return
     */
    public String buildCodeContent(String code) {
        //加载邮件html模板
        Resource resource = new ClassPathResource("mailtemplate.ftl");
        InputStream inputStream = null;
        BufferedReader fileReader = null;
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            inputStream = resource.getStream();
            fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((line = fileReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            log.info("发送邮件读取模板失败{}", e);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //替换html模板中的参数
        return MessageFormat.format(buffer.toString(), mailConfig.getTitle(), mailConfig.getPhone(), mailConfig.getUsername(), code);
    }


    /**
     * 反馈邮件通知
     * @param feedback
     * @param feedbackReply
     * @return
     */
    public String buildFeedbackContent(Feedback feedback, FeedbackReply feedbackReply) {
        //加载邮件html模板
        Resource resource = new ClassPathResource("mail-feedback-template.ftl");
        InputStream inputStream = null;
        BufferedReader fileReader = null;
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            inputStream = resource.getStream();
            fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((line = fileReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            log.info("发送邮件读取模板失败{}", e);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String result = "";
        String feedbackContent = feedback.getFeedbackContent();
        if (feedbackContent.length() > 10) {
            result = feedbackContent.substring(0, 10) + "...";
        } else {
            result = feedbackContent;
        }
        //替换html模板中的参数
        return MessageFormat.format(buffer.toString(), DateUtil.localDateTimeToString(feedback.getFeedbackTime(),
                DateUtil.YYYY_MM_DD_HH_MM_SS),
                result,
                feedback.getFeedbackType(),
                feedback.getFeedbackContent(),
                feedbackReply.getReplyContent(),
                mailConfig.getTitle(),
                mailConfig.getPhone(),
                mailConfig.getUsername());
    }

    /**
     * 反馈邮件通知
     * @param report
     * @param reply
     * @return
     */
    public String buildReportContent(Report report, Report reply) {
        //加载邮件html模板
        Resource resource = new ClassPathResource("mail-report-template.ftl");
        InputStream inputStream = null;
        BufferedReader fileReader = null;
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            inputStream = resource.getStream();
            fileReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            while ((line = fileReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            log.info("发送邮件读取模板失败{}", e);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String result = "";
        String feedbackContent = report.getReportContent();
        if (feedbackContent.length() > 10) {
            result = feedbackContent.substring(0, 10) + "...";
        } else {
            result = feedbackContent;
        }
        //替换html模板中的参数
        return MessageFormat.format(buffer.toString(), DateUtil.localDateTimeToString(report.getReportTime(),
                DateUtil.YYYY_MM_DD_HH_MM_SS),
                result,
                report.getReportType(),
                report.getReportContent(),
                reply.getReportHandleResult(),
                mailConfig.getTitle(),
                mailConfig.getPhone(),
                mailConfig.getUsername());
    }
}
