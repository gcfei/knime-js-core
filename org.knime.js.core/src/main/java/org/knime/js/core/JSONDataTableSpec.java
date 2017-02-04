/*
 * ------------------------------------------------------------------------
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
 * Created on 19.03.2013 by Christian Albrecht, KNIME.com AG, Zurich, Switzerland
 */
package org.knime.js.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.knime.base.data.xml.SvgCell;
import org.knime.base.data.xml.SvgValue;
import org.knime.core.data.BooleanValue;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.DataType;
import org.knime.core.data.DoubleValue;
import org.knime.core.data.MissingCell;
import org.knime.core.data.StringValue;
import org.knime.core.data.date.DateAndTimeCell;
import org.knime.core.data.date.DateAndTimeValue;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.image.png.PNGImageCell;
import org.knime.core.data.image.png.PNGImageValue;
import org.knime.core.data.property.ColorHandler;
import org.knime.js.core.colormodels.JSONColorModel;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 *
 * @author Christian Albrecht, KNIME.com AG, Zurich, Switzerland
 * @since 2.9
 */
@JsonAutoDetect
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class JSONDataTableSpec {

    /**
     *
     * @author Christian Albrecht, KNIME.com AG, Zurich, Switzerland
     */
    public static enum JSTypes {
        /** Boolean JavaScript type */
        BOOLEAN("boolean"),
        /** Number JavaScript type */
        NUMBER("number"),
        /** String JavaScript type */
        STRING("string"),
        /** PNG JavaScript type
         * @since 2.10 */
        PNG("png"),
        /** SVG JavaScript type
         * @since 2.10 */
        SVG("svg"),
        /** Date/Time JavaScript type
         * @since 2.11 */
        DATE_TIME("dateTime"),
        /** Undefined JavaScript type */
        UNDEFINED("undefined");

        private final String m_jsName;

        private JSTypes(final String jsName) {
            m_jsName = jsName;
        }

        private static final Map<String, JSTypes> NAMES_MAP;
        static {
            NAMES_MAP = Stream.of(values()).collect(Collectors.toMap(JSTypes::toString, Function.identity()));
        }

        /**
         * Creates an enum type for a given string representation of a data type
         * @param value the string representation of the data type
         * @return a {@link JSTypes} enum type corresponding to the provided string
         * @throws JsonMappingException if  an invalid data type string was provided
         */
        @JsonCreator
        private static JSTypes forValue(final String value) throws JsonMappingException {
            JSTypes method = NAMES_MAP.get(value.toLowerCase());
            if (method == null) {
                throw new JsonMappingException(null, value + " is not a valid JavaScript table data type.");
            }
            return method;
        }

        /**
         * Returns the name of this type in the JavaScript world.
         *
         * @return the JS name
         */
        @JsonValue
        public String getJSName() {
            return m_jsName;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return m_jsName;
        }
    }

    /**
     * @param colType the column type to determine.
     * @return The corresponding JS column type.
     */
    static JSTypes getJSONType(final DataType colType) {
        JSTypes type;
        if (colType.isCompatible(SvgValue.class)) {
            type = JSTypes.SVG;
        } else if (colType.isCompatible(PNGImageValue.class)) {
            type = JSTypes.PNG;
        } else if (colType.isCompatible(BooleanValue.class)) {
            type = JSTypes.BOOLEAN;
        } else if (colType.isCompatible(DateAndTimeValue.class)) {
            type = JSTypes.DATE_TIME;
        } else if (colType.isCompatible(DoubleValue.class)) {
            type = JSTypes.NUMBER;
        } else if (colType.isCompatible(StringValue.class)) {
            type = JSTypes.STRING;
        } else {
            type = JSTypes.UNDEFINED;
        }

        return type;
    }

    private int m_numColumns;
    private int m_numRows;
    private List<JSTypes> m_colTypes = new ArrayList<JSTypes>();
    private List<String> m_knimeTypes = new ArrayList<String>();
    private List<String> m_colNames = new ArrayList<String>();

    private int m_numExtensions;
    private List<String> m_extensionTypes = new ArrayList<String>();
    private List<String> m_extensionNames = new ArrayList<String>();

    private Vector<LinkedHashSet<Object>> m_possibleValues;
    private Object[] m_minValues;
    private Object[] m_maxValues;

    private String[] m_rowColorValues;
    private String[] m_filterIds;

    private JSONColorModel[] m_colorModels;

    /**
     * Empty default constructor for bean initialization.
     */
    public JSONDataTableSpec() {
        // empty creator for bean initialization
    }

    /**
     * @param spec the DataTableSpec for this JSONTable
     * @param numRows the number of rows in the DataTable
     *
     */
    public JSONDataTableSpec(final DataTableSpec spec, final int numRows) {
        this(spec, new String[0], numRows);
    }

    /**
     * @param spec the DataTableSpec for this JSONTable
     * @param excludeColumns an array of column names to exclude from the creation of the spec
     * @param numRows the number of rows in the DataTable
     *
     */
    public JSONDataTableSpec(final DataTableSpec spec, final String[] excludeColumns, final int numRows) {

        int numColumns = 0;
        List<String> colNames = new ArrayList<String>();
        List<JSTypes> colTypes = new ArrayList<JSTypes>();
        List<String> orgTypes = new ArrayList<String>();
        List<JSONColorModel> colorModels = new ArrayList<JSONColorModel>();
        for (int i = 0; i < spec.getNumColumns(); i++) {
            String colName = spec.getColumnNames()[i];
            if (!Arrays.asList(excludeColumns).contains(colName)) {
                colNames.add(colName);
                orgTypes.add(spec.getColumnSpec(i).getType().getName());
                DataType colType = spec.getColumnSpec(i).getType();
                colTypes.add(getJSONType(colType));
                ColorHandler cHandler = spec.getColumnSpec(i).getColorHandler();
                if (cHandler != null) {
                    colorModels.add(JSONColorModel.createFromColorModel(cHandler.getColorModel(), colName));
                }
                numColumns++;
            }
        }

        setNumColumns(numColumns);
        setNumRows(numRows);
        setColNames(colNames.toArray(new String[0]));
        setColTypes(colTypes.toArray(new JSTypes[0]));
        setKnimeTypes(orgTypes.toArray(new String[0]));
        setColorModels(colorModels.toArray(new JSONColorModel[colorModels.size()]));
    }

    /**
     * Creates a new {@link DataTableSpec} from the current settings.
     * @return the generated spec
     */
    public DataTableSpec createDataTableSpec() {
        DataColumnSpec[] columns = new DataColumnSpec[m_numColumns];
        for (int i = 0; i < m_numColumns; i++) {
            JSTypes type = m_colTypes.get(i);
            DataType dataType = null;
            switch (type) {
                case BOOLEAN:
                    dataType = DataType.getType(BooleanCell.class);
                    break;
                case NUMBER:
                    dataType = DataType.getType(DoubleCell.class);
                    break;
                case DATE_TIME:
                    dataType = DataType.getType(DateAndTimeCell.class);
                    break;
                case STRING:
                    dataType = DataType.getType(StringCell.class);
                    break;
                case SVG:
                    dataType = DataType.getType(SvgCell.class);
                    break;
                case PNG:
                    dataType = DataType.getType(PNGImageCell.class);
                    break;
                default:
                    dataType = DataType.getType(MissingCell.class);
                    break;
            }
            columns[i] = new DataColumnSpecCreator(m_colNames.get(i), dataType).createSpec();
        }
        return new DataTableSpec(columns);
    }

    /**
     * @return the num_columns
     */
    public int getNumColumns() {
        return m_numColumns;
    }

    /**
     * @param num the num_columns to set
     */
    public void setNumColumns(final int num) {
        this.m_numColumns = num;
    }

    /**
     * @return the num_rows
     */
    public int getNumRows() {
        return m_numRows;
    }

    /**
     * @param num the num_rows to set
     */
    public void setNumRows(final int num) {
        this.m_numRows = num;
    }

    /**
     * @return the colNames
     */
    public String[] getColNames() {
        return m_colNames.toArray(new String[0]);
    }

    /**
     * @param names the colNames to set
     */
    public void setColNames(final String[] names) {
        this.m_colNames = new ArrayList<String>();
        this.m_colNames.addAll(Arrays.asList(names));
    }

    /**
     * @return the column types
     */
    public JSTypes[] getColTypes() {
        return m_colTypes.toArray(new JSTypes[0]);
    }

    /**
     * @param types
     */
    public void setColTypes(final JSTypes[] types) {
        m_colTypes = Arrays.asList(types);
    }

    /**
     * @return the knimeTypes
     */
    public String[] getKnimeTypes() {
        return m_knimeTypes.toArray(new String[0]);
    }

    /**
     * @param knimeTypes the knimeTypes to set
     */
    public void setKnimeTypes(final String[] knimeTypes) {
        m_knimeTypes = new ArrayList<String>();
        m_knimeTypes.addAll(Arrays.asList(knimeTypes));
    }

    /**
     * @return
     */
    public int getNumExtensions() {
        return m_numExtensions;
    }

    /**
     * @param num
     */
    public void setNumExtensions(final int num) {
        this.m_numExtensions = num;
    }

    /**
     * @return
     */
    public String[] getExtensionTypes() {
        return m_extensionTypes.toArray(new String[0]);
    }

    /**
     * @param types
     */
    public void setExtensionTypes(final String[] types) {
        this.m_extensionTypes = new ArrayList<String>();
        this.m_extensionTypes.addAll(Arrays.asList(types));
    }

    /**
     * @return
     */
    public String[] getExtensionNames() {
        return m_extensionNames.toArray(new String[0]);
    }

    /**
     * @param names
     */
    public void setExtensionNames(final String[] names) {
        this.m_extensionNames = new ArrayList<String>();
        this.m_extensionNames.addAll(Arrays.asList(names));
    }

    /**
     * @param extensionName
     * @param dataType
     */
    public void addExtension(final String extensionName, final JSTypes dataType) {
        this.m_numExtensions++;
        this.m_extensionNames.add(extensionName);
        this.m_extensionTypes.add(dataType.getJSName());
    }

    /**
     * @return the possibleValues
     */
    public Vector<LinkedHashSet<Object>> getPossibleValues() {
        return m_possibleValues;
    }

    /**
     * @param possibleValues the m_possibleValues to set
     */
    public void setPossibleValues(final Vector<LinkedHashSet<Object>> possibleValues) {
        m_possibleValues = possibleValues;
    }

    /**
     * @return the minValues
     */
    public Object[] getMinValues() {
        return m_minValues;
    }

    /**
     * @param minValues the m_minValues to set
     */
    public void setMinValues(final Object[] minValues) {
        m_minValues = minValues;
    }

    /**
     * @return the maxValues
     */
    public Object[] getMaxValues() {
        return m_maxValues;
    }

    /**
     * @param maxValues the m_maxValues to set
     */
    public void setMaxValues(final Object[] maxValues) {
        m_maxValues = maxValues;
    }

    /**
     * @return the rowColorValues as hex strings
     * @since 2.10
     */
    public String[] getRowColorValues() {
        return m_rowColorValues;
    }

    /**
     * @param rowColorValues the rowColorValues as hex strings to set
     * @since 2.10
     */
    public void setRowColorValues(final String[] rowColorValues) {
        m_rowColorValues = rowColorValues;
    }

    /**
     * @return the filterIds
     * @since 3.3
     */
    public String[] getFilterIds() {
        return m_filterIds;
    }

    /**
     * @param filterIds the filterIds to set
     * @since 3.3
     */
    public void setFilterIds(final String[] filterIds) {
        m_filterIds = filterIds;
    }

    /**
     * @return the colorModels
     */
    public JSONColorModel[] getColorModels() {
        return m_colorModels;
    }

    /**
     * @param colorModels the colorModels to set
     */
    public void setColorModels(final JSONColorModel[] colorModels) {
        m_colorModels = colorModels;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(m_colNames)
                .append(m_knimeTypes)
                .append(m_colTypes)
                .append(m_extensionNames)
                .append(m_extensionTypes)
                .append(m_maxValues)
                .append(m_minValues)
                .append(m_numColumns)
                .append(m_numExtensions)
                .append(m_numRows)
                .append(m_possibleValues)
                .append(m_rowColorValues)
                .append(m_filterIds)
                .append(m_colorModels)
                .toHashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        JSONDataTableSpec other = (JSONDataTableSpec)obj;
        return new EqualsBuilder()
                .append(m_colNames, other.m_colNames)
                .append(m_knimeTypes, other.m_knimeTypes)
                .append(m_colTypes, other.m_colTypes)
                .append(m_extensionNames, other.m_extensionNames)
                .append(m_extensionTypes, other.m_extensionTypes)
                .append(m_maxValues, other.m_maxValues)
                .append(m_minValues, other.m_minValues)
                .append(m_numColumns, other.m_numColumns)
                .append(m_numExtensions, other.m_numExtensions)
                .append(m_numRows, other.m_numRows)
                .append(m_possibleValues, other.m_possibleValues)
                .append(m_rowColorValues, other.m_rowColorValues)
                .append(m_filterIds, other.m_filterIds)
                .append(m_colorModels, other.m_colorModels)
                .isEquals();
    }

}
