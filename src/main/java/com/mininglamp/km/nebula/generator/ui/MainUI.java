package com.mininglamp.km.nebula.generator.ui;

import com.intellij.credentialStore.CredentialAttributes;
import com.intellij.ide.passwordSafe.PasswordSafe;
import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.components.*;
import com.mininglamp.km.nebula.generator.gen.CodeGenerator;
import com.mininglamp.km.nebula.generator.model.Config;
import com.mininglamp.km.nebula.generator.model.User;
import com.mininglamp.km.nebula.generator.setting.PersistentConfig;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author daiyi
 * @date 2021/9/17
 */
public class MainUI extends JFrame {
    private static final Logger logger = Logger.getInstance(MainUI.class);


    private AnActionEvent anActionEvent;
    private Project project;
    private PersistentConfig persistentConfig;
    private Map<String, Config> initConfigMap;
    private Map<String, Config> historyConfigList;
    private Config config;


    private JPanel contentPane = new JBPanel<>();
    private JButton buttonOK = new JButton("OK");
    private JButton buttonCancel = new JButton("CANCEL");
    private JButton selectConfigBtn = new JButton("SELECT");
    private JButton deleteConfigBtn = new JButton("DELETE");


    private JTextField tableNameField = new JTextField(10);
    private JBTextField modelPackageField = new JBTextField(12);
    private JBTextField daoPackageField = new JBTextField(12);
    private JBTextField xmlPackageField = new JBTextField(12);
    private JTextField modelPostfixField = new JTextField(10);
    private JTextField daoPostfixField = new JTextField(10);
    private JTextField mapperPostfixField = new JTextField(10);
    private JTextField modelNameField = new JTextField(10);
    private JTextField keyField = new JTextField(10);

    private TextFieldWithBrowseButton projectFolderBtn = new TextFieldWithBrowseButton();
    private TextFieldWithBrowseButton modelFolderBtn = new TextFieldWithBrowseButton();
    private TextFieldWithBrowseButton daoFolderBtn = new TextFieldWithBrowseButton();
    private TextFieldWithBrowseButton xmlFolderBtn = new TextFieldWithBrowseButton();
    private JTextField modelMvnField = new JBTextField(15);
    private JTextField daoMvnField = new JBTextField(15);
    private JTextField xmlMvnField = new JBTextField(15);
    private JButton setProjectBtn = new JButton("Set-Project-Path");


    public MainUI(AnActionEvent anActionEvent) throws HeadlessException {
        this.anActionEvent = anActionEvent;
        this.project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        this.persistentConfig = PersistentConfig.getInstance();

        initConfigMap = persistentConfig.getInitConfig();
        historyConfigList = persistentConfig.getHistoryConfigList();


        setTitle("nebula generate tool");
        setPreferredSize(new Dimension(1300, 700));//设置大小
        setLocation(120, 100);
        pack();
        setVisible(true);
        getRootPane().setDefaultButton(buttonOK);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);


        String projectFolder = project.getBasePath();


        if (initConfigMap != null) {
            config = initConfigMap.get("initConfig");
        }


        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout());

        JPanel paneMain = new JPanel(new GridLayout(2, 1, 3, 3));//主要设置显示在这里
        JPanel paneMainTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        paneMainTop.setBorder(new EmptyBorder(10, 30, 5, 40));

        JPanel paneMainTop1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel paneMainTop2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel paneMainTop3 = new JPanel(new GridLayout(4, 1, 3, 3));
        paneMainTop.add(paneMainTop1);
        paneMainTop.add(paneMainTop2);
        paneMainTop.add(paneMainTop3);


        JPanel paneLeft1 = new JPanel();
        paneLeft1.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel tablejLabel = new JLabel("table  name:");
        tablejLabel.setSize(new Dimension(20, 30));
        paneLeft1.add(tablejLabel);
        paneLeft1.add(tableNameField);


        JPanel paneRight1 = new JPanel();
        paneRight1.setLayout(new FlowLayout(FlowLayout.CENTER));
        paneRight1.add(new JLabel("model postfix:"));
        if (config != null && !StringUtils.isEmpty(config.getModelPostfix())) {
            modelPostfixField.setText(config.getModelPostfix());
        } else {
            modelPostfixField.setText("DO");
        }
        paneRight1.add(modelPostfixField);

        JPanel paneRight2 = new JPanel();
        paneRight2.setLayout(new FlowLayout(FlowLayout.CENTER));
        paneRight2.add(new JLabel("dao postfix:"));
        if (config != null && !StringUtils.isEmpty(config.getDaoPostfix())) {
            daoPostfixField.setText(config.getDaoPostfix());
        } else {
            daoPostfixField.setText("Dao");
        }
        paneRight2.add(daoPostfixField);

        JPanel paneRight3 = new JPanel();
        paneRight3.setLayout(new FlowLayout(FlowLayout.CENTER));
        paneRight3.add(new JLabel("mapper postfix:"));
        if (config != null && !StringUtils.isEmpty(config.getMapperPostfix())) {
            mapperPostfixField.setText(config.getMapperPostfix());
        } else {
            mapperPostfixField.setText("Mapper");
        }
        paneRight3.add(mapperPostfixField);

        paneMainTop1.add(paneLeft1);
        paneMainTop1.add(paneRight1);
        paneMainTop1.add(paneRight2);
        paneMainTop1.add(paneRight3);

        JPanel modelPackagePanel = new JPanel();
        modelPackagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JBLabel labelLeft4 = new JBLabel("java file package:");
        modelPackagePanel.add(labelLeft4);
        if (config != null && !StringUtils.isEmpty(config.getModelPackage())) {
            modelPackageField.setText(config.getModelPackage());
        } else {
            modelPackageField.setText("generator");
        }
        modelPackagePanel.add(modelPackageField);
        JButton packageBtn1 = new JButton("...");
        packageBtn1.addActionListener(actionEvent -> {
            final PackageChooserDialog chooser = new PackageChooserDialog("chooser java file package", project);
            chooser.selectPackage(modelPackageField.getText());
            chooser.show();
            final PsiPackage psiPackage = chooser.getSelectedPackage();
            String packageName = psiPackage == null ? "generator" : psiPackage.getQualifiedName();
            modelPackageField.setText(packageName);
            MainUI.this.toFront();
        });
        modelPackagePanel.add(packageBtn1);


//        JPanel daoPackagePanel = new JPanel();
//        daoPackagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
//        JLabel labelLeft5 = new JLabel("dao package:");
//        daoPackagePanel.add(labelLeft5);
//
//
//        if (config != null && !StringUtils.isEmpty(config.getDaoPackage())) {
//            daoPackageField.setText(config.getDaoPackage());
//        } else {
//            daoPackageField.setText("generator");
//        }
//        daoPackagePanel.add(daoPackageField);
//
//        JButton packageBtn2 = new JButton("...");
//        packageBtn2.addActionListener(actionEvent -> {
//            final PackageChooserDialog chooser = new PackageChooserDialog("choose dao package", project);
//            chooser.selectPackage(daoPackageField.getText());
//            chooser.show();
//            final PsiPackage psiPackage = chooser.getSelectedPackage();
//            String packageName = psiPackage == null ? null : psiPackage.getQualifiedName();
//            daoPackageField.setText(packageName);
//            MainUI.this.toFront();
//        });
//        daoPackagePanel.add(packageBtn2);

        JPanel xmlPackagePanel = new JPanel();
        xmlPackagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel labelLeft6 = new JLabel("xml package:");
        xmlPackagePanel.add(labelLeft6);
        if (config != null && !StringUtils.isEmpty(config.getXmlPackage())) {
            xmlPackageField.setText(config.getXmlPackage());
        } else {
            xmlPackageField.setText("generator");
        }
        xmlPackagePanel.add(xmlPackageField);

        paneMainTop2.add(modelPackagePanel);
//        paneMainTop2.add(daoPackagePanel);
        paneMainTop2.add(xmlPackagePanel);


        JPanel projectFolderPanel = new JPanel();
        projectFolderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel projectLabel = new JLabel("project folder:");
        projectFolderPanel.add(projectLabel);
        projectFolderBtn.setTextFieldPreferredWidth(45);
        if (config != null && !StringUtils.isEmpty(config.getProjectFolder())) {
            projectFolderBtn.setText(config.getProjectFolder());
        } else {
            projectFolderBtn.setText(projectFolder);
        }
        projectFolderBtn.addBrowseFolderListener(new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                super.actionPerformed(e);
                projectFolderBtn.setText(projectFolderBtn.getText().replaceAll("\\\\", "/"));
            }
        });
        projectFolderPanel.add(projectFolderBtn);
        projectFolderPanel.add(setProjectBtn);


        JPanel modelFolderPanel = new JPanel();
        modelFolderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        modelFolderPanel.add(new JLabel("model  folder:"));

        modelFolderBtn.setTextFieldPreferredWidth(45);
        if (config != null && !StringUtils.isEmpty(config.getModelTargetFolder())) {
            modelFolderBtn.setText(config.getModelTargetFolder());
        } else {
            modelFolderBtn.setText(projectFolder);
        }
        modelFolderBtn.addBrowseFolderListener(new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                super.actionPerformed(e);
                modelFolderBtn.setText(modelFolderBtn.getText().replaceAll("\\\\", "/"));
            }
        });
        modelFolderPanel.add(modelFolderBtn);
        modelFolderPanel.add(new JLabel("mvn path:"));
        modelMvnField.setText("src/main/java");
        modelFolderPanel.add(modelMvnField);


        JPanel daoFolderPanel = new JPanel();
        daoFolderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        daoFolderPanel.add(new JLabel("dao     folder:"));
        daoFolderBtn.setTextFieldPreferredWidth(45);
        if (config != null && !StringUtils.isEmpty(config.getDaoTargetFolder())) {
            daoFolderBtn.setText(config.getDaoTargetFolder());
        } else {
            daoFolderBtn.setText(projectFolder);
        }
        daoFolderBtn.addBrowseFolderListener(new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor()) {
            @Override
            public void actionPerformed(ActionEvent e) {
                super.actionPerformed(e);
                daoFolderBtn.setText(daoFolderBtn.getText().replaceAll("\\\\", "/"));
            }
        });
        daoFolderPanel.add(daoFolderBtn);
        daoFolderPanel.add(new JLabel("mvn path:"));
        daoMvnField.setText("src/main/java");
        daoFolderPanel.add(daoMvnField);


        JPanel xmlFolderPanel = new JPanel();
        xmlFolderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        xmlFolderPanel.add(new JLabel("xml     folder:"));

        xmlFolderBtn.setTextFieldPreferredWidth(45);
        if (config != null && !StringUtils.isEmpty(config.getXmlTargetFolder())) {
            xmlFolderBtn.setText(config.getXmlTargetFolder());
        } else {
            xmlFolderBtn.setText(projectFolder);
        }
        xmlFolderBtn.addBrowseFolderListener(new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor()) {
        });
        xmlFolderPanel.add(xmlFolderBtn);
        xmlFolderPanel.add(new JLabel("mvn path:"));
        xmlMvnField.setText("src/main/resources");
        xmlFolderPanel.add(xmlMvnField);

        paneMainTop3.add(projectFolderPanel);
        paneMainTop3.add(modelFolderPanel);
        paneMainTop3.add(daoFolderPanel);
        paneMainTop3.add(xmlFolderPanel);


        JBPanel paneMainDown = new JBPanel(new GridLayout(5, 5, 5, 5));
        paneMainDown.setBorder(new EmptyBorder(2, 80, 100, 40));


        paneMain.add(paneMainTop);
        paneMain.add(paneMainDown);


        JPanel paneBottom = new JPanel();//确认和取消按钮
        paneBottom.setLayout(new FlowLayout(2));
        paneBottom.add(buttonOK);
        paneBottom.add(buttonCancel);


        JPanel panelLeft = new JPanel();
        panelLeft.setLayout(new BoxLayout(panelLeft, BoxLayout.Y_AXIS));
        this.getContentPane().add(Box.createVerticalStrut(10)); //采用x布局时，添加固定宽度组件隔开
        final DefaultListModel defaultListModel = new DefaultListModel();

        Border historyBorder = BorderFactory.createTitledBorder("history config:");
        panelLeft.setBorder(historyBorder);


        if (historyConfigList == null) {
            historyConfigList = new HashMap<>();
        }
        for (String historyConfigName : historyConfigList.keySet()) {
            defaultListModel.addElement(historyConfigName);
        }
        Map<String, Config> finalHistoryConfigList = historyConfigList;

        final JBList fruitList = new JBList(defaultListModel);
        fruitList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fruitList.setSelectedIndex(0);
        fruitList.setVisibleRowCount(25);
        JBScrollPane ScrollPane = new JBScrollPane(fruitList);
        panelLeft.add(ScrollPane);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.X_AXIS));

        btnPanel.add(selectConfigBtn);
        btnPanel.add(deleteConfigBtn);
        selectConfigBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String configName = (String) fruitList.getSelectedValue();
                if (StringUtils.isEmpty(configName)) {
                    return;
                }
                Config selectedConfig = finalHistoryConfigList.get(configName);
                tableNameField.setText(selectedConfig.getTableName());
                modelPostfixField.setText(selectedConfig.getModelPostfix());
                daoPostfixField.setText(selectedConfig.getDaoPostfix());
                mapperPostfixField.setText(selectedConfig.getMapperPostfix());
                modelPackageField.setText(selectedConfig.getModelPackage());
                daoPackageField.setText(selectedConfig.getDaoPackage());
                xmlPackageField.setText(selectedConfig.getXmlPackage());
                projectFolderBtn.setText(selectedConfig.getProjectFolder());
                modelFolderBtn.setText(selectedConfig.getModelTargetFolder());
                daoFolderBtn.setText(selectedConfig.getDaoTargetFolder());
                xmlFolderBtn.setText(selectedConfig.getXmlTargetFolder());
                modelMvnField.setText(selectedConfig.getModelMvnPath());
                daoMvnField.setText(selectedConfig.getDaoMvnPath());
                xmlMvnField.setText(selectedConfig.getXmlMvnPath());
            }
        });
        deleteConfigBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalHistoryConfigList.remove(fruitList.getSelectedValue());
                defaultListModel.removeAllElements();
                for (String historyConfigName : finalHistoryConfigList.keySet()) {
                    defaultListModel.addElement(historyConfigName);
                }
            }
        });
        panelLeft.add(btnPanel);


        contentPane.add(paneMain, BorderLayout.CENTER);
        contentPane.add(paneBottom, BorderLayout.SOUTH);
        contentPane.add(panelLeft, BorderLayout.WEST);

        setContentPane(contentPane);

        setProjectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelFolderBtn.setText(projectFolderBtn.getText());
                daoFolderBtn.setText(projectFolderBtn.getText());
                xmlFolderBtn.setText(projectFolderBtn.getText());
            }
        });

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
                onOK();
                Thread.currentThread().setContextClassLoader(contextClassLoader);

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

        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        try {
            PersistentConfig instance = PersistentConfig.getInstance();
            if (Objects.isNull(instance) || Objects.isNull(instance.getInitConfig())) {
                Messages.showMessageDialog("nebula url has not set", "Error", null);
                return;
            }
            Config initConfig = instance.getInitConfig().get("initConfig");
            String url = initConfig.getUrl();

            if (StringUtils.isEmpty(tableNameField.getText())) {
                Messages.showMessageDialog("table is null", "Error", null);
                return;
            }


            Config generatorConfig = new Config();
            generatorConfig.setName(tableNameField.getText());
            generatorConfig.setTableName(tableNameField.getText());
            generatorConfig.setProjectFolder(projectFolderBtn.getText());

            generatorConfig.setModelPackage(modelPackageField.getText());
            generatorConfig.setModelTargetFolder(modelFolderBtn.getText());
            generatorConfig.setDaoPackage(daoPackageField.getText());
            generatorConfig.setDaoTargetFolder(daoFolderBtn.getText());
            generatorConfig.setXmlPackage(xmlPackageField.getText());
            generatorConfig.setXmlTargetFolder(xmlFolderBtn.getText());

            generatorConfig.setModelMvnPath(modelMvnField.getText());
            generatorConfig.setDaoMvnPath(daoMvnField.getText());
            generatorConfig.setXmlMvnPath(xmlMvnField.getText());

            generatorConfig.setModelPostfix(modelPostfixField.getText());
            generatorConfig.setDaoPostfix(daoPostfixField.getText());
            generatorConfig.setMapperPostfix(mapperPostfixField.getText());


            dispose();

            Map<String, User> users = instance.getUsers();
            if (Objects.isNull(users)) {
                new UserUI(anActionEvent, initConfig.getUrl(), generatorConfig);
                return;
            }

            User user = users.get(url);
            if (Objects.isNull(user)) {
                new UserUI(anActionEvent, initConfig.getUrl(), generatorConfig);
                return;
            }
            //连接数据库
            connect2Nebula(url, user.getUsername(), generatorConfig);

            // 代码生成
            CodeGenerator genInstance = CodeGenerator.getInstance();
            if (genInstance.gen(generatorConfig, url, user.getUsername())) {
                //生成记录保存
                saveConfig(tableNameField.getText(), generatorConfig);
                // 更新目录
                VirtualFileManager.getInstance().syncRefresh();
            } else {
                //生成失败再次展示配置页面
                new MainUI(anActionEvent);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            dispose();
        }
    }

    /**
     * 连接数据库，第一次连接成功保存用户配置
     */
    private void connect2Nebula(String url, String username, Config generatorConfig) {


        Map<String, User> users = persistentConfig.getUsers();
        if (users != null && users.containsKey(url)) {
            CredentialAttributes attributes_get = new CredentialAttributes("nebula-mybatis-generator-" + url, username, this.getClass(), false);
            String password = PasswordSafe.getInstance().getPassword(attributes_get);
            if (StringUtils.isEmpty(password)) {
                new UserUI(anActionEvent, url, generatorConfig);
            }
        } else {
            new UserUI(anActionEvent, url, generatorConfig);
        }

    }

    /**
     * 保存当前配置到历史记录
     *
     * @param tableName 表名
     * @param config    配置
     */
    private void saveConfig(String tableName, Config config) {
        Map<String, Config> historyConfigList = persistentConfig.getHistoryConfigList();
        if (historyConfigList == null) {
            historyConfigList = new HashMap<>();
        }
        historyConfigList.put(tableName, config);
        persistentConfig.setHistoryConfigList(historyConfigList);
        logger.info("nebula-generator history 记录成功");
    }


    private void onCancel() {
        dispose();
    }

}
