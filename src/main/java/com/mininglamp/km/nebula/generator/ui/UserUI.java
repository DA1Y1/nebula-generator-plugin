package com.mininglamp.km.nebula.generator.ui;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.credentialStore.Credentials;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextField;
import com.mininglamp.km.nebula.generator.gen.CodeGenerator;
import com.mininglamp.km.nebula.generator.model.Config;
import com.mininglamp.km.nebula.generator.model.DbType;
import com.mininglamp.km.nebula.generator.model.User;
import com.mininglamp.km.nebula.generator.setting.PersistentConfig;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

/**
 * 账号密码输入界面
 *
 * @author daiyi
 * @date 2021/9/17
 */
public class UserUI extends JFrame {

    private static final Logger logger = Logger.getInstance(UserUI.class);


    private AnActionEvent anActionEvent;
    private Project project;
    private PersistentConfig persistentConfig;
    private JPanel contentPanel = new JBPanel<>();
    private JPanel btnPanel = new JBPanel<>();
    private JPanel filedPanel = new JBPanel<>();
    private JButton buttonOK = new JButton("OK");
    private JButton buttonCancel = new JButton("CANCEL");

    public JTextField usernameField = new JBTextField(20);
    public JTextField passwordField = new JBTextField(20);


    public UserUI(AnActionEvent anActionEvent, String address, Config generatorConfig) throws HeadlessException {
        this.anActionEvent = anActionEvent;
        this.project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        this.persistentConfig = PersistentConfig.getInstance();
        setTitle("set nebula connection");
        setPreferredSize(new Dimension(400, 180));//设置大小
        setLocation(550, 350);
        pack();
        setVisible(true);
        getRootPane().setDefaultButton(buttonOK);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setContentPane(contentPanel);

        filedPanel.setLayout(new GridLayout(2, 1));
        filedPanel.setBorder(new EmptyBorder(8, 2, 2, 2));

        JPanel panel1 = new JBPanel<>();
        panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
        panel1.setBorder(new EmptyBorder(2, 2, 2, 2));

        JPanel panel2 = new JBPanel<>();
        panel2.setLayout(new BoxLayout(panel2, BoxLayout.X_AXIS));
        panel2.setBorder(new EmptyBorder(2, 2, 2, 2));

        panel1.add(new JLabel("username:"));
        panel1.add(usernameField);
        panel2.add(new JLabel("password:"));
        panel2.add(passwordField);

        filedPanel.add(panel1);
        filedPanel.add(panel2);
        contentPanel.add(filedPanel);

        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));
        btnPanel.setBorder(new EmptyBorder(1, 5, 1, 5));
        btnPanel.add(buttonOK);
        btnPanel.add(buttonCancel);
        contentPanel.add(btnPanel);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK(address, persistentConfig, project, generatorConfig);
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        contentPanel.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }


    private void onOK(String address, PersistentConfig persistentConfig, Project project, Config generatorConfig) {
        try {
            Connection conn = null;
            try {
                Class.forName(DbType.NEBULA.getDriverClass());

                conn = DriverManager.getConnection(address, usernameField.getText(), passwordField.getText());

                Map<String, User> users = persistentConfig.getUsers();
                if (users == null) {
                    users = new HashMap<>();
                }
                users.put(address, new User(usernameField.getText()));
                CredentialAttributes attributes = new CredentialAttributes("nebula-mybatis-generator-" + address, usernameField.getText(), this.getClass(), false);
                Credentials saveCredentials = new Credentials(attributes.getUserName(), passwordField.getText());
                PasswordSafe.getInstance().set(attributes, saveCredentials);
                persistentConfig.setUsers(users);
                logger.info("nebula-generator nebula 连接成功");

                // VirtualFile baseDir = ProjectUtil.guessProjectDir(project);
                // baseDir.refresh(false, true);
                Messages.showInfoMessage("connect success", "Set success");

                //连接成功直接生成文件
                CodeGenerator genInstance = CodeGenerator.getInstance();
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
                boolean success = genInstance.gen(generatorConfig, address, usernameField.getText());
                Thread.currentThread().setContextClassLoader(contextClassLoader);
                // 更新目录
                VirtualFileManager.getInstance().syncRefresh();
                //生成历史记录
                if (success) {
                    //生成记录保存
                    Map<String, Config> historyConfigList = persistentConfig.getHistoryConfigList();
                    if (historyConfigList == null) {
                        historyConfigList = new HashMap<>();
                    }
                    historyConfigList.put(generatorConfig.getTableName(), generatorConfig);
                    persistentConfig.setHistoryConfigList(historyConfigList);
                    logger.info("nebula-generator history 记录成功");
                } else {
                    new MainUI(anActionEvent);
                }
            } catch (Exception e) {
                Messages.showInfoMessage("nebula connection fail,check config", "Set fail");
                logger.warn("nebula-generator nebula 连接失败 " + e.getMessage(), e);
                // new MainUI(anActionEvent);
                new UserUI(anActionEvent, address, generatorConfig);
            } finally {
                if (conn != null) {
                    conn.close();
                }
                dispose();
            }


        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            dispose();
        }
    }

    private void onCancel() {
        dispose();
        new MainUI(anActionEvent);
    }
}
