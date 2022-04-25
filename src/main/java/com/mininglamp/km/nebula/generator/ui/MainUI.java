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

import static com.mininglamp.km.nebula.generator.tools.Constants.*;

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


    private JPanel contentPanel = new JBPanel<>();
    private JButton buttonOK = new JButton("OK");
    private JButton buttonCancel = new JButton("CANCEL");
    private JButton selectConfigBtn = new JButton("SELECT");
    private JButton deleteConfigBtn = new JButton("DELETE");


    private JTextField tableNameField = new JTextField(10);
    private JBTextField modelPackageField = new JBTextField(12);
    private JBTextField daoPackageField = new JBTextField(12);
    private JBTextField xmlPackageField = new JBTextField(12);
    private JTextField modelSuffixField = new JTextField(8);
    private JTextField daoSuffixField = new JTextField(8);
    private JTextField mapperSuffixField = new JTextField(8);

    private TextFieldWithBrowseButton projectFolderBtn = new TextFieldWithBrowseButton();
    private TextFieldWithBrowseButton modelFolderBtn = new TextFieldWithBrowseButton();
    private TextFieldWithBrowseButton daoFolderBtn = new TextFieldWithBrowseButton();
    private TextFieldWithBrowseButton xmlFolderBtn = new TextFieldWithBrowseButton();
    private JTextField modelMvnField = new JBTextField(15);
    private JTextField daoMvnField = new JBTextField(15);
    private JTextField xmlMvnField = new JBTextField(15);
    private JButton setProjectBtn = new JButton(SET_PROJECT_PATH);


    public MainUI(AnActionEvent anActionEvent) throws HeadlessException {
        this.anActionEvent = anActionEvent;
        this.project = anActionEvent.getData(PlatformDataKeys.PROJECT);
        this.persistentConfig = PersistentConfig.getInstance();

        initConfigMap = persistentConfig.getInitConfig();
        historyConfigList = persistentConfig.getHistoryConfigList();


        setTitle("Nebula Generator Tool");
        setPreferredSize(new Dimension(1000, 600));//设置大小
        setLocation(120, 100);
        pack();
        setVisible(true);
        getRootPane().setDefaultButton(buttonOK);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);


        String projectFolder = project.getBasePath();


        if (initConfigMap != null) {
            config = initConfigMap.get("initConfig");
        }


        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPanel.setLayout(new BorderLayout());

        JPanel panelMain = new JPanel(new GridLayout(1, 1, 3, 3));//主要设置显示在这里
        JPanel panelMainTop = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelMainTop.setBorder(new EmptyBorder(10, 30, 5, 40));

        JPanel panelMainTop1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelMainTop2 = new JPanel(new GridLayout(4, 1, 3, 3));
        JPanel panelMainTop3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelMainTop.add(panelMainTop1);
        panelMainTop.add(panelMainTop2);
        panelMainTop.add(panelMainTop3);


        JPanel panelLeft1 = new JPanel();
        panelLeft1.setLayout(new FlowLayout(FlowLayout.CENTER));
        JLabel tableLabel = new JLabel(TABLE_NAME);
        tableLabel.setSize(new Dimension(20, 30));
        panelLeft1.add(tableLabel);
        panelLeft1.add(tableNameField);
        panelMainTop1.add(panelLeft1);



        JPanel panelRight1 = new JPanel();
        panelRight1.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelRight1.add(new JLabel(MODEL_SUFFIX));
        if (config != null && !StringUtils.isEmpty(config.getModelSuffix())) {
            modelSuffixField.setText(config.getModelSuffix());
        } else {
            modelSuffixField.setText(DO);
        }
        panelRight1.add(modelSuffixField);

        JPanel panelRight2 = new JPanel();
        panelRight2.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelRight2.add(new JLabel(DAO_SUFFIX));
        if (config != null && !StringUtils.isEmpty(config.getDaoSuffix())) {
            daoSuffixField.setText(config.getDaoSuffix());
        } else {
            daoSuffixField.setText(DAO);
        }
        panelRight2.add(daoSuffixField);

        JPanel panelRight3 = new JPanel();
        panelRight3.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelRight3.add(new JLabel(MAPPER_SUFFIX));
        if (config != null && !StringUtils.isEmpty(config.getMapperSuffix())) {
            mapperSuffixField.setText(config.getMapperSuffix());
        } else {
            mapperSuffixField.setText(MAPPER);
        }
        panelRight3.add(mapperSuffixField);

        panelMainTop1.add(panelRight1);
        panelMainTop1.add(panelRight2);
        panelMainTop1.add(panelRight3);

        JPanel modelPackagePanel = new JPanel();
        modelPackagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JBLabel labelLeft4 = new JBLabel(JAVA_FILE_PACKAGE);
        modelPackagePanel.add(labelLeft4);
        if (config != null && !StringUtils.isEmpty(config.getModelPackage())) {
            modelPackageField.setText(config.getModelPackage());
        } else {
            modelPackageField.setText(GENERATOR);
        }
        modelPackagePanel.add(modelPackageField);
        JButton packageBtn1 = new JButton("...");
        packageBtn1.addActionListener(actionEvent -> {
            final PackageChooserDialog chooser = new PackageChooserDialog("chooser java file package", project);
            chooser.selectPackage(modelPackageField.getText());
            chooser.show();
            final PsiPackage psiPackage = chooser.getSelectedPackage();
            String packageName = psiPackage == null ? GENERATOR : psiPackage.getQualifiedName();
            modelPackageField.setText(packageName);
            MainUI.this.toFront();
        });
        modelPackagePanel.add(packageBtn1);


        JPanel xmlPackagePanel = new JPanel();
        xmlPackagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel labelLeft6 = new JLabel(XML_PACKAGE);
        xmlPackagePanel.add(labelLeft6);
        if (config != null && !StringUtils.isEmpty(config.getXmlPackage())) {
            xmlPackageField.setText(config.getXmlPackage());
        } else {
            xmlPackageField.setText(GENERATOR);
        }
        xmlPackagePanel.add(xmlPackageField);

        panelMainTop3.add(modelPackagePanel);
        panelMainTop3.add(xmlPackagePanel);


        JPanel projectFolderPanel = new JPanel();
        projectFolderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel projectLabel = new JLabel(PROJECT_FOLDER);
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
        modelFolderPanel.add(new JLabel(MODEL_FOLDER));
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
        modelFolderPanel.add(new JLabel(MVN_PATH));
        modelMvnField.setText(SRC_MAIN_JAVA);
        modelFolderPanel.add(modelMvnField);


        JPanel daoFolderPanel = new JPanel();
        daoFolderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        daoFolderPanel.add(new JLabel(DAO_FOLDER));
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
        daoFolderPanel.add(new JLabel(MVN_PATH));
        daoMvnField.setText(SRC_MAIN_JAVA);
        daoFolderPanel.add(daoMvnField);


        JPanel xmlFolderPanel = new JPanel();
        xmlFolderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        xmlFolderPanel.add(new JLabel(XML_FOLDER));
        xmlFolderBtn.setTextFieldPreferredWidth(45);
        if (config != null && !StringUtils.isEmpty(config.getXmlTargetFolder())) {
            xmlFolderBtn.setText(config.getXmlTargetFolder());
        } else {
            xmlFolderBtn.setText(projectFolder);
        }
        xmlFolderBtn.addBrowseFolderListener(new TextBrowseFolderListener(FileChooserDescriptorFactory.createSingleFileOrFolderDescriptor()) {
        });
        xmlFolderPanel.add(xmlFolderBtn);
        xmlFolderPanel.add(new JLabel(MVN_PATH));
        xmlMvnField.setText(SRC_MAIN_RESOURCES);
        xmlFolderPanel.add(xmlMvnField);

        panelMainTop2.add(projectFolderPanel);
        panelMainTop2.add(modelFolderPanel);
        panelMainTop2.add(daoFolderPanel);
        panelMainTop2.add(xmlFolderPanel);


//        JBPanel paneMainDown = new JBPanel(new GridLayout(5, 5, 5, 5));
//        paneMainDown.setBorder(new EmptyBorder(2, 80, 100, 40));


        panelMain.add(panelMainTop);
//        panelMain.add(paneMainDown);


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
                modelSuffixField.setText(selectedConfig.getModelSuffix());
                daoSuffixField.setText(selectedConfig.getDaoSuffix());
                mapperSuffixField.setText(selectedConfig.getMapperSuffix());
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


        contentPanel.add(panelMain, BorderLayout.CENTER);
        contentPanel.add(paneBottom, BorderLayout.SOUTH);
        contentPanel.add(panelLeft, BorderLayout.WEST);

        setContentPane(contentPanel);

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

        contentPanel.registerKeyboardAction(new ActionListener() {
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

            generatorConfig.setModelSuffix(modelSuffixField.getText());
            generatorConfig.setDaoSuffix(daoSuffixField.getText());
            generatorConfig.setMapperSuffix(mapperSuffixField.getText());


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
