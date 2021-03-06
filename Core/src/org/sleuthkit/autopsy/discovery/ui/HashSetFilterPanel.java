/*
 * Autopsy
 *
 * Copyright 2020 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.discovery.ui;

import org.sleuthkit.autopsy.discovery.search.AbstractFilter;
import java.util.List;
import java.util.logging.Level;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import org.openide.util.NbBundle;
import org.sleuthkit.autopsy.coreutils.Logger;
import org.sleuthkit.autopsy.coreutils.ThreadConfined;
import org.sleuthkit.autopsy.discovery.search.SearchFiltering;
import org.sleuthkit.datamodel.BlackboardArtifact;
import org.sleuthkit.datamodel.BlackboardAttribute;
import org.sleuthkit.datamodel.TskCoreException;

/**
 * Panel for displaying the Hash Set filter controls.
 */
final class HashSetFilterPanel extends AbstractDiscoveryFilterPanel {

    private static final long serialVersionUID = 1L;
    private final static Logger logger = Logger.getLogger(HashSetFilterPanel.class.getName());

    /**
     * Creates new form HashSetFilterPaenl.
     */
    @ThreadConfined(type = ThreadConfined.ThreadType.AWT)
    HashSetFilterPanel() {
        initComponents();
        setUpHashFilter();
    }

    /**
     * Initialize the hash filter.
     */
    @ThreadConfined(type = ThreadConfined.ThreadType.AWT)
    private void setUpHashFilter() {
        int count = 0;
        try {
            DefaultListModel<String> hashListModel = (DefaultListModel<String>) hashSetList.getModel();
            hashListModel.removeAllElements();
            List<String> setNames = DiscoveryUiUtils.getSetNames(BlackboardArtifact.ARTIFACT_TYPE.TSK_HASHSET_HIT,
                    BlackboardAttribute.ATTRIBUTE_TYPE.TSK_SET_NAME);
            for (String name : setNames) {
                hashListModel.add(count, name);
                count++;
            }
        } catch (TskCoreException ex) {
            logger.log(Level.SEVERE, "Error loading hash set names", ex);
            hashSetCheckbox.setEnabled(false);
            hashSetList.setEnabled(false);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        hashSetCheckbox = new javax.swing.JCheckBox();
        hashSetScrollPane = new javax.swing.JScrollPane();
        hashSetList = new javax.swing.JList<>();

        org.openide.awt.Mnemonics.setLocalizedText(hashSetCheckbox, org.openide.util.NbBundle.getMessage(HashSetFilterPanel.class, "HashSetFilterPanel.hashSetCheckbox.text")); // NOI18N
        hashSetCheckbox.setMaximumSize(new java.awt.Dimension(150, 25));
        hashSetCheckbox.setMinimumSize(new java.awt.Dimension(150, 25));
        hashSetCheckbox.setPreferredSize(new java.awt.Dimension(150, 25));
        hashSetCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hashSetCheckboxActionPerformed(evt);
            }
        });

        setMinimumSize(new java.awt.Dimension(250, 30));
        setPreferredSize(new java.awt.Dimension(250, 30));

        hashSetList.setModel(new DefaultListModel<String>());
        hashSetList.setEnabled(false);
        hashSetList.setVisibleRowCount(3);
        hashSetScrollPane.setViewportView(hashSetList);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hashSetScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(hashSetScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void hashSetCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hashSetCheckboxActionPerformed
        hashSetList.setEnabled(hashSetCheckbox.isSelected());
    }//GEN-LAST:event_hashSetCheckboxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox hashSetCheckbox;
    private javax.swing.JList<String> hashSetList;
    private javax.swing.JScrollPane hashSetScrollPane;
    // End of variables declaration//GEN-END:variables

    @ThreadConfined(type = ThreadConfined.ThreadType.AWT)
    @Override
    void configurePanel(boolean selected, int[] indicesSelected) {
        boolean hasHashSets = hashSetList.getModel().getSize() > 0;
        hashSetCheckbox.setEnabled(hasHashSets);
        hashSetCheckbox.setSelected(selected && hasHashSets);
        if (hashSetCheckbox.isEnabled() && hashSetCheckbox.isSelected()) {
            hashSetScrollPane.setEnabled(true);
            hashSetList.setEnabled(true);
            if (indicesSelected != null) {
                hashSetList.setSelectedIndices(indicesSelected);
            }
        } else {
            hashSetScrollPane.setEnabled(false);
            hashSetList.setEnabled(false);
        }
    }

    @ThreadConfined(type = ThreadConfined.ThreadType.AWT)
    @Override
    JCheckBox getCheckbox() {
        return hashSetCheckbox;
    }

    @Override
    JLabel getAdditionalLabel() {
        return null;
    }

    @ThreadConfined(type = ThreadConfined.ThreadType.AWT)
    @NbBundle.Messages({"HashSetFilterPanel.error.text=At least one hash set name must be selected."})
    @Override
    String checkForError() {
        if (hashSetCheckbox.isSelected() && hashSetList.getSelectedValuesList().isEmpty()) {
            return Bundle.HashSetFilterPanel_error_text();
        }
        return "";
    }

    @ThreadConfined(type = ThreadConfined.ThreadType.AWT)
    @Override
    JList<?> getList() {
        return hashSetList;
    }

    @ThreadConfined(type = ThreadConfined.ThreadType.AWT)
    @Override
    AbstractFilter getFilter() {
        if (hashSetCheckbox.isSelected()) {
            return new SearchFiltering.HashSetFilter(hashSetList.getSelectedValuesList());
        }
        return null;
    }
}
