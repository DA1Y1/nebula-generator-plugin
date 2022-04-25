package com.mininglamp.km.nebula.generator.ui;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextField;
import com.mininglamp.km.nebula.generator.model.Config;
import com.mininglamp.km.nebula.generator.model.DbType;
import com.mininglamp.km.nebula.generator.setting.PersistentConfig;
import com.mininglamp.km.nebula.generator.tools.JTextFieldHintListener;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


/**
 * @author daiyi
 * @date 2021/9/17
 */
public class SettingUI extends JDialog {

    private static final Logger logger = Logger.getInstance(SettingUI.class);

    public JPanel mainPanel = new JBPanel<>(new GridLayout());
    private String projectPath = "";


    private JTextField urlField = new JTextField(50);
    private JTextField modelPostfixField = new JTextField(10);
    private JTextField daoPostfixField = new JTextField(10);
    private JTextField mapperPostfixField = new JTextField(10);
    private JBTextField modelPackageField = new JBTextField(12);
    private JBTextField daoPackageField = new JBTextField(12);
    private JBTextField xmlPackageField = new JBTextField(12);

    private JButton setProjectBtn = new JButton("Set-Project-Path");
    private TextFieldWithBrowseButton projectFolderBtn = new TextFieldWithBrowseButton();
    private TextFieldWithBrowseButton modelFolderBtn = new TextFieldWithBrowseButton();
    private TextFieldWithBrowseButton daoFolderBtn = new TextFieldWithBrowseButton();
    private TextFieldWithBrowseButton xmlFolderBtn = new TextFieldWithBrowseButton();
    private JTextField modelMvnField = new JBTextField(15);
    private JTextField daoMvnField = new JBTextField(15);
    private JTextField xmlMvnField = new JBTextField(15);

    private PersistentConfig config;

    public SettingUI() {
        setContentPane(mainPanel);
    }


    public void createUI(Project project) {
        config = PersistentConfig.getInstance();
        Map<String, Config> initConfigMap = config.getInitConfig();


        String projectFolder = project.getBasePath();
        projectPath = project.getBasePath();
        mainPanel.setPreferredSize(new Dimension(0, 0));
        JPanel paneMainCenter = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JPanel panelMainTopAll = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel paneMainTop1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel paneMainTop2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel paneMainTop3 = new JPanel(new GridLayout(4, 1, 3, 3));
        paneMainCenter.add(panelMainTopAll);
        paneMainCenter.add(paneMainTop1);
        paneMainCenter.add(paneMainTop2);
        paneMainCenter.add(paneMainTop3);

        JPanel panelUrl = new JPanel();
        panelUrl.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelUrl.add(new JLabel("nebula url:"));
        panelUrl.add(urlField);


        panelMainTopAll.add(panelUrl);

        JPanel paneRight1 = new JPanel();
        paneRight1.setLayout(new FlowLayout(FlowLayout.CENTER));
        paneRight1.add(new JLabel("model postfix:"));
        modelPostfixField.setText("DO");
        paneRight1.add(modelPostfixField);
        JPanel paneRight2 = new JPanel();
        paneRight2.setLayout(new FlowLayout(FlowLayout.CENTER));
        paneRight2.add(new JLabel("dao postfix:"));
        daoPostfixField.setText("Dao");
        paneRight2.add(daoPostfixField);
        JPanel paneRight3 = new JPanel();
        paneRight3.setLayout(new FlowLayout(FlowLayout.CENTER));
        paneRight3.add(new JLabel("mapper postfix:"));
        mapperPostfixField.setText("Mapper");
        paneRight3.add(mapperPostfixField);


        paneMainTop1.add(paneRight1);
        paneMainTop1.add(paneRight2);
        paneMainTop1.add(paneRight3);

        JPanel modelPackagePanel = new JPanel();
        modelPackagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JBLabel labelLeft4 = new JBLabel("java file package:");
        modelPackagePanel.add(labelLeft4);
        modelPackagePanel.add(modelPackageField);
        JButton packageBtn1 = new JButton("...");
        packageBtn1.addActionListener(actionEvent -> {
            final PackageChooserDialog chooser = new PackageChooserDialog("chooser java file package", project);
            chooser.selectPackage(modelPackageField.getText());
            chooser.show();
            final PsiPackage psiPackage = chooser.getSelectedPackage();
            String packageName = psiPackage == null ? "generator" : psiPackage.getQualifiedName();
            modelPackageField.setText(packageName);
        });
        modelPackagePanel.add(packageBtn1);


        JPanel xmlPackagePanel = new JPanel();
        xmlPackagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel labelLeft6 = new JLabel("xml package:");
        xmlPackagePanel.add(labelLeft6);
        xmlPackagePanel.add(xmlPackageField);

        paneMainTop2.add(modelPackagePanel);
        paneMainTop2.add(xmlPackagePanel);


        JPanel projectFolderPanel = new JPanel();
        projectFolderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel projectLabel = new JLabel("project folder:");
        projectFolderPanel.add(projectLabel);
        projectFolderBtn.setTextFieldPreferredWidth(45);
        projectFolderBtn.setText(projectFolder);
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
        modelFolderBtn.setText(projectFolder);
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
        daoFolderPanel.add(new JLabel("dao      folder:"));
        daoFolderBtn.setTextFieldPreferredWidth(45);
        daoFolderBtn.setText(projectFolder);
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
        xmlFolderPanel.add(new JLabel("xml      folder:"));


        xmlFolderBtn.setTextFieldPreferredWidth(45);
        xmlFolderBtn.setText(projectFolder);
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


        mainPanel.add(paneMainCenter);

        setProjectBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modelFolderBtn.setText(projectFolderBtn.getText());
                daoFolderBtn.setText(projectFolderBtn.getText());
                xmlFolderBtn.setText(projectFolderBtn.getText());
            }
        });

        if (Objects.nonNull(initConfigMap)) {
            Config configValue = initConfigMap.get("initConfig");
            urlField.setText(configValue.getUrl());
            modelPostfixField.setText(configValue.getModelPostfix());
            daoPostfixField.setText(configValue.getDaoPostfix());
            mapperPostfixField.setText(configValue.getMapperPostfix());
            modelPackageField.setText(configValue.getModelPackage());
            daoPackageField.setText(configValue.getDaoPackage());
            xmlPackageField.setText(configValue.getXmlPackage());

            projectFolderBtn.setText(configValue.getProjectFolder());
            modelFolderBtn.setText(configValue.getModelTargetFolder());
            daoFolderBtn.setText(configValue.getDaoTargetFolder());
            xmlFolderBtn.setText(configValue.getXmlTargetFolder());

        } else {
            urlField.addFocusListener(new JTextFieldHintListener(urlField, "nebula url"));
            modelPackageField.addFocusListener(new JTextFieldHintListener(modelPackageField, "generator"));
            xmlPackageField.addFocusListener(new JTextFieldHintListener(xmlPackageField, "generator"));
        }

    }

    public boolean isModified() {
        Map<String, Config> initConfig = config.getInitConfig();
        //未配置时与默认配置比较
        if (Objects.isNull(initConfig)) {
            if (!StringUtils.equals(this.urlField.getText(), "nebula url") && !StringUtils.equals(this.urlField.getText(), "")) {
                return true;
            }
            if (!StringUtils.equals(this.modelPostfixField.getText(), "DO") && !StringUtils.equals(this.modelPostfixField.getText(), "")) {
                return true;
            }
            if (!StringUtils.equals(this.daoPostfixField.getText(), "Dao") && !StringUtils.equals(this.daoPostfixField.getText(), "")) {
                return true;
            }
            if (!StringUtils.equals(this.mapperPostfixField.getText(), "Mapper") && !StringUtils.equals(this.mapperPostfixField.getText(), "")) {
                return true;
            }
            if (!StringUtils.equals(this.modelPackageField.getText(), "generator") && !StringUtils.equals(this.modelPackageField.getText(), "")) {
                return true;
            }
            if (!StringUtils.equals(this.xmlPackageField.getText(), "generator") && !StringUtils.equals(this.xmlPackageField.getText(), "")) {
                return true;
            }
            if (!StringUtils.equals(this.projectFolderBtn.getText(), projectPath) && !StringUtils.equals(this.projectFolderBtn.getText(), "")) {
                return true;
            }
            if (!StringUtils.equals(this.modelFolderBtn.getText(), projectPath) && !StringUtils.equals(this.modelFolderBtn.getText(), "")) {
                return true;
            }
            if (!StringUtils.equals(this.daoFolderBtn.getText(), projectPath) && !StringUtils.equals(this.daoFolderBtn.getText(), "")) {
                return true;
            }
            if (!StringUtils.equals(this.xmlFolderBtn.getText(), projectPath) && !StringUtils.equals(this.xmlFolderBtn.getText(), "")) {
                return true;
            }
            if (!StringUtils.equals(this.modelMvnField.getText(), "src/main/java") && !StringUtils.equals(this.modelMvnField.getText(), "")) {
                return true;
            }
            if (!StringUtils.equals(this.daoMvnField.getText(), "src/main/java") && !StringUtils.equals(this.daoMvnField.getText(), "")) {
                return true;
            }
            if (!StringUtils.equals(this.xmlMvnField.getText(), "src/main/resources") && !StringUtils.equals(this.xmlMvnField.getText(), "")) {
                return true;
            }
            return false;
        }
        //保存过配置时 与保存配置比较
        Config config = initConfig.get("initConfig");
        if (Objects.isNull(config)) {
            return false;
        }
        if (!checkSettingModify(this.urlField.getText(), config.getUrl())) {
            return true;
        }
        if (!checkSettingModify(this.modelPostfixField.getText(), config.getModelPostfix())) {
            return true;
        }
        if (!checkSettingModify(this.daoPostfixField.getText(), config.getDaoPostfix())) {
            return true;
        }
        if (!checkSettingModify(this.mapperPostfixField.getText(), config.getMapperPostfix())) {
            return true;
        }
        if (!checkSettingModify(this.modelPackageField.getText(), config.getModelPackage())) {
            return true;
        }
        if (!checkSettingModify(this.xmlPackageField.getText(), config.getXmlPackage())) {
            return true;
        }
        if (!checkSettingModify(this.projectFolderBtn.getText(), config.getProjectFolder())) {
            return true;
        }
        if (!checkSettingModify(this.modelFolderBtn.getText(), config.getModelTargetFolder())) {
            return true;
        }
        if (!checkSettingModify(this.daoFolderBtn.getText(), config.getDaoTargetFolder())) {
            return true;
        }
        if (!checkSettingModify(this.xmlFolderBtn.getText(), config.getXmlTargetFolder())) {
            return true;
        }
        if (!checkSettingModify(this.modelMvnField.getText(), config.getModelMvnPath())) {
            return true;
        }
        if (!checkSettingModify(this.daoMvnField.getText(), config.getDaoMvnPath())) {
            return true;
        }
        if (!checkSettingModify(this.xmlMvnField.getText(), config.getXmlMvnPath())) {
            return true;
        }
        return false;
    }

    public void apply() {
        if (StringUtils.equals("nebula url", urlField.getText()) || StringUtils.isEmpty(urlField.getText())) {
            Messages.showMessageDialog("nebula url is null", "Error", null);
            return;
        }
        if (!urlField.getText().startsWith("jdbc:graph://")) {
            Messages.showMessageDialog("Connect Example" + DbType.NEBULA.getConnectionUrlPattern(), "Error", null);
            return;
        }
        HashMap<String, Config> initConfig = new HashMap<>();
        Config config = new Config();
        config.setName("initConfig");
        config.setUrl(urlField.getText());
        config.setModelPostfix(modelPostfixField.getText());
        config.setDaoPostfix(daoPostfixField.getText());
        config.setMapperPostfix(mapperPostfixField.getText());
        config.setModelPackage(modelPackageField.getText());
        config.setDaoPackage(daoPackageField.getText());
        config.setXmlPackage(xmlPackageField.getText());
        config.setProjectFolder(projectFolderBtn.getText());
        config.setModelTargetFolder(modelFolderBtn.getText());
        config.setDaoTargetFolder(daoFolderBtn.getText());
        config.setXmlTargetFolder(xmlFolderBtn.getText());
        config.setModelMvnPath(modelMvnField.getText());
        config.setDaoMvnPath(daoMvnField.getText());
        config.setXmlMvnPath(xmlMvnField.getText());

        initConfig.put(config.getName(), config);
        this.config.setInitConfig(initConfig);
        logger.info("nebula-generator setting apply操作成功 ");
    }


    public void reset() {
        Map<String, Config> initConfig = config.getInitConfig();
        if (Objects.nonNull(initConfig) && Objects.nonNull(initConfig.get("initConfig"))) {
            Config config = initConfig.get("initConfig");
            this.urlField.setText(config.getUrl());
            this.modelPostfixField.setText(config.getModelPostfix());
            this.daoPostfixField.setText(config.getDaoPostfix());
            this.mapperPostfixField.setText(config.getMapperPostfix());
            this.modelPackageField.setText(config.getModelPackage());
            this.xmlPackageField.setText(config.getXmlPackage());
            this.projectFolderBtn.setText(config.getProjectFolder());
            this.modelFolderBtn.setText(config.getModelTargetFolder());
            this.daoFolderBtn.setText(config.getDaoTargetFolder());
            this.xmlFolderBtn.setText(config.getXmlTargetFolder());
            this.modelMvnField.setText(config.getModelMvnPath());
            this.daoMvnField.setText(config.getDaoMvnPath());
            this.xmlMvnField.setText(config.getXmlMvnPath());
        } else {
            this.urlField.setText("");
            this.urlField.addFocusListener(new JTextFieldHintListener(urlField, "nebula url"));
            this.modelPostfixField.setText("DO");
            this.daoPostfixField.setText("Dao");
            this.mapperPostfixField.setText("Mapper");
            this.modelPackageField.setText("");
            this.xmlPackageField.setText("");
            modelPackageField.addFocusListener(new JTextFieldHintListener(modelPackageField, "generator"));
            xmlPackageField.addFocusListener(new JTextFieldHintListener(xmlPackageField, "generator"));
            this.projectFolderBtn.setText(projectPath);
            this.modelFolderBtn.setText(projectPath);
            this.daoFolderBtn.setText(projectPath);
            this.xmlFolderBtn.setText(projectPath);
            this.modelMvnField.setText("src/main/java");
            this.daoMvnField.setText("src/main/java");
            this.xmlMvnField.setText("src/main/resources");
        }
        // logger.info("nebula-generator setting reset操作成功");

    }

    @Override
    public JPanel getContentPane() {
        return mainPanel;
    }

    /**
     * 检查配置是否修改
     *
     * @param fieldContext  当前配置的内容
     * @param configContext 保存配置的内容
     * @return
     */
    public boolean checkSettingModify(String fieldContext, String configContext) {
        if (StringUtils.isEmpty(configContext)) {
            return false;
        }
        return StringUtils.equals(fieldContext, configContext);
    }
}


