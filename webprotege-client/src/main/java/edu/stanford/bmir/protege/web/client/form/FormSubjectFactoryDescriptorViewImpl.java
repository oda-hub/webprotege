package edu.stanford.bmir.protege.web.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import edu.stanford.bmir.protege.web.client.editor.ValueListEditor;
import edu.stanford.bmir.protege.web.client.editor.ValueListFlexEditorImpl;
import edu.stanford.bmir.protege.web.client.primitive.PrimitiveDataEditor;
import edu.stanford.bmir.protege.web.client.ui.Counter;
import edu.stanford.bmir.protege.web.shared.entity.OWLClassData;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.entity.OWLPrimitiveData;
import org.semanticweb.owlapi.model.EntityType;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2020-01-15
 */
public class FormSubjectFactoryDescriptorViewImpl extends Composite implements FormSubjectFactoryDescriptorView {

    @UiField(provided = true)
    protected static Counter counter = new Counter();

    private static FormSubjectFactoryDescriptorViewImplUiBinder ourUiBinder = GWT.create(
            FormSubjectFactoryDescriptorViewImplUiBinder.class);

    @UiField
    RadioButton classRadio;

    @UiField
    RadioButton individualRadio;

    @UiField(provided = true)
    PrimitiveDataEditor parentEditor;

    @UiField
    TextBox generatedNamePattern;

    @Inject
    public FormSubjectFactoryDescriptorViewImpl(PrimitiveDataEditor primitiveDataEditor) {
        counter.increment();
        parentEditor = primitiveDataEditor;
        parentEditor.setEnabled(true);
        initWidget(ourUiBinder.createAndBindUi(this));
    }

    @Override
    public void clear() {
        parentEditor.clearValue();
    }

    @Nonnull
    @Override
    public EntityType<?> getEntityType() {
        if(individualRadio.getValue()) {
            return EntityType.NAMED_INDIVIDUAL;
        }
        else {
            return EntityType.CLASS;
        }
    }

    @Override
    public void setEntityType(@Nonnull EntityType<?> entityType) {
        if(entityType.equals(EntityType.NAMED_INDIVIDUAL)) {
            individualRadio.setValue(true);
        }
        else {
            classRadio.setValue(true);
        }
    }

    @Override
    public void setParentClass(@Nonnull OWLClassData parent) {
        parentEditor.setValue(parent);
    }

    @Nonnull
    @Override
    public Optional<OWLClassData> getParentClass() {
        return parentEditor.getValue()
                .filter(parent -> parent instanceof OWLClassData)
                .map(parent -> (OWLClassData) parent);
    }

    @Override
    public void setGeneratedNamePattern(@Nonnull String generatedNamePattern) {
        this.generatedNamePattern.setText(generatedNamePattern);
    }

    @Nonnull
    @Override
    public String getGeneratedNamePattern() {
        return generatedNamePattern.getText().trim();
    }

    interface FormSubjectFactoryDescriptorViewImplUiBinder extends UiBinder<HTMLPanel, FormSubjectFactoryDescriptorViewImpl> {

    }

}
