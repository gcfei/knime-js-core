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
 *  KNIME Integereroperates with ECLIPSE solely via ECLIPSE's plug-in APIs.
 *  Hence, KNIME and ECLIPSE are both independent programs and are not
 *  derived from each other. Should, however, the Integererpretation of the
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
 *  NodeDialog, and NodeView) and that only Integereroperate with KNIME through
 *  standard APIs ("Nodes"):
 *  Nodes are deemed to be separate and independent programs and to not be
 *  covered works.  Notwithstanding anything to the contrary in the
 *  License, the License does not apply to Nodes, you are not required to
 *  license Nodes under the License, and you are granted a license to
 *  prepare and propagate Nodes, in each case even if such Nodes are
 *  propagated with or for Integereroperation with KNIME.  The owner of a Node
 *  may freely choose the license terms applicable to such Node, including
 *  when such Node is propagated with or for Integereroperation with KNIME.
 * ---------------------------------------------------------------------
 *
 * History
 *   Nov 11, 2015 (albrecht): created
 */
package org.knime.js.core.layout.bs;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 *
 * @author Christian Albrecht, KNIME.com GmbH, Konstanz, Germany
 */
@JsonAutoDetect
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
public class JSONLayoutColumn extends JSONLayoutElement {

    private Integer m_widthXS;
    private Integer m_widthSM;
    private Integer m_widthMD;
    private Integer m_widthLG;
    private Integer m_widthXL;

    private List<JSONLayoutContent> m_content;

    /**
     * @return the widthXS
     */
    public Integer getWidthXS() {
        return m_widthXS;
    }

    /**
     * @param widthXS the widthXS to set
     */
    public void setWidthXS(final Integer widthXS) {
        m_widthXS = widthXS;
    }

    /**
     * @return the widthSM
     */
    public Integer getWidthSM() {
        return m_widthSM;
    }

    /**
     * @param widthSM the widthSM to set
     */
    public void setWidthSM(final Integer widthSM) {
        m_widthSM = widthSM;
    }

    /**
     * @return the widthMD
     */
    public Integer getWidthMD() {
        return m_widthMD;
    }

    /**
     * @param widthMD the widthMD to set
     */
    public void setWidthMD(final Integer widthMD) {
        m_widthMD = widthMD;
    }

    /**
     * @return the widthLG
     */
    public Integer getWidthLG() {
        return m_widthLG;
    }

    /**
     * @param widthLG the widthLG to set
     */
    public void setWidthLG(final Integer widthLG) {
        m_widthLG = widthLG;
    }

    /**
     * @return the widthXL
     */
    public Integer getWidthXL() {
        return m_widthXL;
    }

    /**
     * @param widthXL the widthXL to set
     */
    public void setWidthXL(final Integer widthXL) {
        m_widthXL = widthXL;
    }

    /**
     * @return the content
     */
    public List<JSONLayoutContent> getContent() {
        return m_content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(final List<JSONLayoutContent> content) {
        m_content = content;
    }



}
