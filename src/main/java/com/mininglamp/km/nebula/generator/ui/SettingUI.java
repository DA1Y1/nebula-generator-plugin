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

import static com.mininglamp.km.nebula.generator.tools.Constants.*;

/**
 * @author daiyi
 * @date 2021/9/17
 */
public class SettingUI extends JDialog {

    private static final Logger logger = Logger.getInstance(SettingUI.class);


    public JPanel mainPanel = new JBPanel<>(new GridLayout());
    private String projectPath = "";


    private JTextField urlField = new JTextField(50);
    private JTextField modelSuffixField = new JTextField(10);
    private JTextField daoSuffixField = new JTextField(10);
    private JTextField mapperSuffixField = new JTextField(10);
    private JBTextField modelPackageField = new JBTextField(12);
    private JBTextField daoPackageField = new JBTextField(12);
    private JBTextField xmlPackageField = new JBTextField(12);

    private JButton setProjectBtn = new JButton(SET_PROJECT_PATH);
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
        JPanel panelMainCenter = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JPanel panelMainTopAll = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelMainTop1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel panelMainTop2 = new JPanel(new GridLayout(4, 1, 3, 3));
        JPanel panelMainTop3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelMainCenter.add(panelMainTopAll);
        panelMainCenter.add(panelMainTop1);
        panelMainCenter.add(panelMainTop2);
        panelMainCenter.add(panelMainTop3);

        JPanel panelUrl = new JPanel();
        panelUrl.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelUrl.add(new JLabel(NEBULA_URL));
        panelUrl.add(urlField);


        panelMainTopAll.add(panelUrl);

        JPanel panelRight1 = new JPanel();
        panelRight1.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelRight1.add(new JLabel(MODEL_SUFFIX));
        modelSuffixField.setText(DO);
        panelRight1.add(modelSuffixField);
        JPanel panelRight2 = new JPanel();
        panelRight2.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelRight2.add(new JLabel(DAO_SUFFIX));
        daoSuffixField.setText(DAO);
        panelRight2.add(daoSuffixField);
        JPanel panelRight3 = new JPanel();
        panelRight3.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelRight3.add(new JLabel(MAPPER_SUFFIX));
        mapperSuffixField.setText(MAPPER);
        panelRight3.add(mapperSuffixField);


        panelMainTop1.add(panelRight1);
        panelMainTop1.add(panelRight2);
        panelMainTop1.add(panelRight3);

        JPanel modelPackagePanel = new JPanel();
        modelPackagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JBLabel labelLeft4 = new JBLabel(JAVA_FILE_PACKAGE);
        modelPackagePanel.add(labelLeft4);
        modelPackagePanel.add(modelPackageField);
        JButton packageBtn1 = new JButton("...");
        packageBtn1.addActionListener(actionEvent -> {
            final PackageChooserDialog chooser = new PackageChooserDialog("chooser java file package", project);
            chooser.selectPackage(modelPackageField.getText());
            chooser.show();
            final PsiPackage psiPackage = chooser.getSelectedPackage();
            String packageName = psiPackage == null ? GENERATOR : psiPackage.getQualifiedName();
            modelPackageField.setText(packageName);
        });
        modelPackagePanel.add(packageBtn1);


        JPanel xmlPackagePanel = new JPanel();
        xmlPackagePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel labelLeft6 = new JLabel(XML_PACKAGE);
        xmlPackagePanel.add(labelLeft6);
        xmlPackagePanel.add(xmlPackageField);

        panelMainTop3.add(modelPackagePanel);
        panelMainTop3.add(xmlPackagePanel);


        JPanel projectFolderPanel = new JPanel();
        projectFolderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel projectLabel = new JLabel(PROJECT_FOLDER);
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
        modelFolderPanel.add(new JLabel(MODEL_FOLDER));

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
        modelFolderPanel.add(new JLabel(MVN_PATH));
        modelMvnField.setText(SRC_MAIN_JAVA);
        modelFolderPanel.add(modelMvnField);


        JPanel daoFolderPanel = new JPanel();
        daoFolderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        daoFolderPanel.add(new JLabel(DAO_FOLDER));
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
        daoFolderPanel.add(new JLabel(MVN_PATH));
        daoMvnField.setText(SRC_MAIN_JAVA);
        daoFolderPanel.add(daoMvnField);


        JPanel xmlFolderPanel = new JPanel();
        xmlFolderPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        xmlFolderPanel.add(new JLabel(XML_FOLDER));


        xmlFolderBtn.setTextFieldPreferredWidth(45);
        xmlFolderBtn.setText(projectFolder);
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


        mainPanel.add(panelMainCenter);

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
            modelSuffixField.setText(configValue.getModelSuffix());
            daoSuffixField.setText(configValue.getDaoSuffix());
            mapperSuffixField.setText(configValue.getMapperSuffix());
            modelPackageField.setText(configValue.getModelPackage());
            daoPackageField.setText(configValue.getDaoPackage());
            xmlPackageField.setText(configValue.getXmlPackage());

            projectFolderBtn.setText(configValue.getProjectFolder());
            modelFolderBtn.setText(configValue.getModelTargetFolder());
            daoFolderBtn.setText(configValue.getDaoTargetFolder());
            xmlFolderBtn.setText(configValue.getXmlTargetFolder());

        } else {
            urlField.addFocusListener(new JTextFieldHintListener(urlField, NEBULA_URL));
            modelPackageField.addFocusListener(new JTextFieldHintListener(modelPackageField, GENERATOR));
            xmlPackageField.addFocusListener(new JTextFieldHintListener(xmlPackageField, GENERATOR));
        }

    }

    public boolean isModified() {
        Map<String, Config> initConfig = config.getInitConfig();
        //?????????????????????????????????
        if (Objects.isNull(initConfig)) {
            if (!StringUtils.equals(this.urlField.getText(), NEBULA_URL)) {
                return true;
            }
            if (!StringUtils.equals(this.modelSuffixField.getText(), DO)) {
                return true;
            }
            if (!StringUtils.equals(this.daoSuffixField.getText(), DAO)) {
                return true;
            }
            if (!StringUtils.equals(this.mapperSuffixField.getText(), MAPPER)) {
                return true;
            }
            if (!StringUtils.equals(this.modelPackageField.getText(), GENERATOR)) {
                return true;
            }
            if (!StringUtils.equals(this.xmlPackageField.getText(), GENERATOR)) {
                return true;
            }
            if (!StringUtils.equals(this.projectFolderBtn.getText(), projectPath)) {
                return true;
            }
            if (!StringUtils.equals(this.modelFolderBtn.getText(), projectPath)) {
                return true;
            }
            if (!StringUtils.equals(this.daoFolderBtn.getText(), projectPath)) {
                return true;
            }
            if (!StringUtils.equals(this.xmlFolderBtn.getText(), projectPath)) {
                return true;
            }
            if (!StringUtils.equals(this.modelMvnField.getText(), SRC_MAIN_JAVA)) {
                return true;
            }
            if (!StringUtils.equals(this.daoMvnField.getText(), SRC_MAIN_JAVA)) {
                return true;
            }
            if (!StringUtils.equals(this.xmlMvnField.getText(), SRC_MAIN_RESOURCES)) {
                return true;
            }
            return false;
        }
        //?????????????????? ?????????????????????
        Config config = initConfig.get("initConfig");
        if (Objects.isNull(config)) {
            return false;
        }
        if (!checkSettingModify(this.urlField.getText(), config.getUrl())) {
            return true;
        }
        if (!checkSettingModify(this.modelSuffixField.getText(), config.getModelSuffix())) {
            return true;
        }
        if (!checkSettingModify(this.daoSuffixField.getText(), config.getDaoSuffix())) {
            return true;
        }
        if (!checkSettingModify(this.mapperSuffixField.getText(), config.getMapperSuffix())) {
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
        if (StringUtils.equals(NEBULA_URL, urlField.getText()) || StringUtils.isEmpty(urlField.getText())) {
            Messages.showMessageDialog("Nebula url is null", "Error", null);
            return;
        }
        if (!urlField.getText().startsWith("jdbc:nebula://")) {
            Messages.showMessageDialog("Connect example: " + DbType.NEBULA.getConnectionUrlPattern(), "Error", null);
            return;
        }
        HashMap<String, Config> initConfig = new HashMap<>();
        Config config = new Config();
        config.setName("initConfig");
        config.setUrl(urlField.getText());
        config.setModelSuffix(modelSuffixField.getText());
        config.setDaoSuffix(daoSuffixField.getText());
        config.setMapperSuffix(mapperSuffixField.getText());
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
        logger.info("nebula-generator setting apply???????????? ");
    }


    public void reset() {
        Map<String, Config> initConfig = config.getInitConfig();
        if (Objects.nonNull(initConfig) && Objects.nonNull(initConfig.get("initConfig"))) {
            Config config = initConfig.get("initConfig");
            this.urlField.setText(config.getUrl());
            this.modelSuffixField.setText(config.getModelSuffix());
            this.daoSuffixField.setText(config.getDaoSuffix());
            this.mapperSuffixField.setText(config.getMapperSuffix());
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
            this.urlField.addFocusListener(new JTextFieldHintListener(urlField, NEBULA_URL));
            this.modelSuffixField.setText(DO);
            this.daoSuffixField.setText(DAO);
            this.mapperSuffixField.setText(MAPPER);
            this.modelPackageField.setText("");
            this.xmlPackageField.setText("");
            modelPackageField.addFocusListener(new JTextFieldHintListener(modelPackageField, GENERATOR));
            xmlPackageField.addFocusListener(new JTextFieldHintListener(xmlPackageField, GENERATOR));
            this.projectFolderBtn.setText(projectPath);
            this.modelFolderBtn.setText(projectPath);
            this.daoFolderBtn.setText(projectPath);
            this.xmlFolderBtn.setText(projectPath);
            this.modelMvnField.setText(SRC_MAIN_JAVA);
            this.daoMvnField.setText(SRC_MAIN_JAVA);
            this.xmlMvnField.setText(SRC_MAIN_RESOURCES);
        }
        // logger.info("nebula-generator setting reset????????????");

    }

    @Override
    public JPanel getContentPane() {
        return mainPanel;
    }

    /**
     * ????????????????????????
     *
     * @param fieldContext  ?????????????????????
     * @param configContext ?????????????????????
     * @return
     */
    public boolean checkSettingModify(String fieldContext, String configContext) {
        if (StringUtils.isEmpty(configContext)) {
            return false;
        }
        return StringUtils.equals(fieldContext, configContext);
    }
}


