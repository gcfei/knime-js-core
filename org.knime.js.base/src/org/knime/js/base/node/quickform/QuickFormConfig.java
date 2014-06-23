/*
 * ------------------------------------------------------------------------
 *
 *  Copyright by KNIME GmbH, Konstanz, Germany
 *  Website: http://www.knime.org; Email: contact@knime.org
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License, Version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 *  Additional permission under GNU GPL version 3 section 7:
 *
 *  KNIME interoperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the interpretation of the
 *  GNU GPL Version 3 ("License") under any applicable laws result in
 *  KNIME and ECLIPSE being a combined program, KNIME GMBH herewith grants
 *  you the additional permission to use and propagate KNIME together with
 *  ECLIPSE with only the license terms in place for ECLIPSE applying to
 *  ECLIPSE and the GNU GPL Version 3 applying for KNIME, provided the
 *  license terms of ECLIPSE themselves allow for the respective use and
 *  propagation of ECLIPSE together with KNIME.
 *
 *  Additional permission relating to nodes for KNIME that extend the Node
 *  Extension (and in particular that are based on subclasses of NodeModel,
 *  NodeDialog, and NodeView) and that only interoperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for interoperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for interoperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * History
 *   Jun 12, 2014 (winter): created
 */
package org.knime.js.base.node.quickform;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.dialog.DialogNodeValue;

/**
 *
 * @author winter
 */
public abstract class QuickFormConfig<VAL extends DialogNodeValue> {

    private static final String CFG_LABEL = "label";
    private static final String CFG_DESCRIPTION = "description";
    private static final String CFG_HIDE_IN_WIZARD = "hideInWizard";
    private static final String CFG_HIDE_IN_DIALOG = "hideInDialog";
    private static final String CFG_DEFAULT_VALUE = "defaultVale";

    private static final String DEFAULT_LABEL = "Label";
    private static final String DEFAULT_DESCRIPTION = "Enter Description";
    private static final boolean DEFAULT_HIDE_IN_WIZARD = false;
    private static final boolean DEFAULT_HIDE_IN_DIALOG = false;

    private String m_label = DEFAULT_LABEL;
    private String m_description = DEFAULT_DESCRIPTION;
    private boolean m_hideInWizard = DEFAULT_HIDE_IN_WIZARD;
    private boolean m_hideInDialog = DEFAULT_HIDE_IN_DIALOG;
    private VAL m_defaultValue = createEmptyValue();

    public String getLabel() {
        return m_label;
    }

    public void setLabel(final String label) {
        this.m_label = label;
    }

    public String getDescription() {
        return m_description;
    }

    public void setDescription(final String description) {
        this.m_description = description;
    }

    public boolean getHideInWizard() {
        return m_hideInWizard;
    }

    public void setHideInWizard(final boolean hideInWizard) {
        m_hideInWizard = hideInWizard;
    }

    public boolean getHideInDialog() {
        return m_hideInDialog;
    }

    public void setHideInDialog(final boolean hideInDialog) {
        m_hideInDialog = hideInDialog;
    }

    public void saveSettings(final NodeSettingsWO settings) {
        NodeSettingsWO defaultValueSettings = settings.addNodeSettings(CFG_DEFAULT_VALUE);
        m_defaultValue.saveToNodeSettings(defaultValueSettings);
        settings.addString(CFG_LABEL, m_label);
        settings.addString(CFG_DESCRIPTION, m_description);
        settings.addBoolean(CFG_HIDE_IN_WIZARD, m_hideInWizard);
        settings.addBoolean(CFG_HIDE_IN_DIALOG, m_hideInDialog);
    }

    public void loadSettings(final NodeSettingsRO settings) throws InvalidSettingsException {
        NodeSettingsRO defaultValueSettings = settings.getNodeSettings(CFG_DEFAULT_VALUE);
        m_defaultValue = createEmptyValue();
        m_defaultValue.loadFromNodeSettings(defaultValueSettings);
        m_label = settings.getString(CFG_LABEL);
        m_description = settings.getString(CFG_DESCRIPTION);
        m_hideInWizard = settings.getBoolean(CFG_HIDE_IN_WIZARD);
        m_hideInDialog = settings.getBoolean(CFG_HIDE_IN_DIALOG);
    }

    public void loadSettingsInDialog(final NodeSettingsRO settings) {
        m_defaultValue = createEmptyValue();
        NodeSettingsRO defaultValueSettings;
        try {
            defaultValueSettings = settings.getNodeSettings(CFG_DEFAULT_VALUE);
            m_defaultValue.loadFromNodeSettingsInDialog(defaultValueSettings);
        } catch (InvalidSettingsException e) {
            // Stay with defaults
        }
        m_label = settings.getString(CFG_LABEL, DEFAULT_LABEL);
        m_description = settings.getString(CFG_DESCRIPTION, DEFAULT_DESCRIPTION);
        m_hideInWizard = settings.getBoolean(CFG_HIDE_IN_WIZARD, DEFAULT_HIDE_IN_WIZARD);
        m_hideInDialog = settings.getBoolean(CFG_HIDE_IN_DIALOG, DEFAULT_HIDE_IN_DIALOG);
    }

    public VAL getDefaultValue() {
        return m_defaultValue;
    }

    protected abstract VAL createEmptyValue();

}