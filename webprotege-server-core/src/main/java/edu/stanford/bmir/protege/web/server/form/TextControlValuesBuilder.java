package edu.stanford.bmir.protege.web.server.form;

import com.google.common.collect.ImmutableList;
import edu.stanford.bmir.protege.web.shared.entity.OWLEntityData;
import edu.stanford.bmir.protege.web.shared.form.data.FormControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.TextControlDataDto;
import edu.stanford.bmir.protege.web.shared.form.data.TextControlDataDtoComparator;
import edu.stanford.bmir.protege.web.shared.form.field.OwlBinding;
import edu.stanford.bmir.protege.web.shared.form.field.TextControlDescriptor;
import edu.stanford.bmir.protege.web.shared.lang.LangTag;
import edu.stanford.bmir.protege.web.shared.lang.LangTagFilter;
import org.semanticweb.owlapi.model.OWLLiteral;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import static com.google.common.base.Preconditions.checkNotNull;

@FormDataBuilderSession
public class TextControlValuesBuilder {

    @Nonnull
    private final BindingValuesExtractor bindingValuesExtractor;

    @Nonnull
    private final TextControlDataDtoComparator textControlDataDtoComparator;

    @Inject
    public TextControlValuesBuilder(@Nonnull BindingValuesExtractor bindingValuesExtractor,
                                    @Nonnull TextControlDataDtoComparator textControlDataDtoComparator) {
        this.bindingValuesExtractor = checkNotNull(bindingValuesExtractor);
        this.textControlDataDtoComparator = checkNotNull(textControlDataDtoComparator);
    }

    @Nonnull
    public ImmutableList<FormControlDataDto> getTextControlDataDtoValues(@Nonnull TextControlDescriptor textControlDescriptor,
                                                                  @Nonnull OWLEntityData subject,
                                                                  @Nonnull OwlBinding theBinding,
                                                                  @Nonnull LangTagFilter langTagFilter) {
        var values = bindingValuesExtractor.getBindingValues(subject.getEntity(), theBinding);
        return values.stream()
                     .filter(p -> p instanceof OWLLiteral)
                     .map(p -> (OWLLiteral) p)
                     .filter(literal ->  isIncluded(literal, langTagFilter))
                     .map(literal -> TextControlDataDto.get(textControlDescriptor, literal))
                     .sorted(textControlDataDtoComparator)
                     .collect(ImmutableList.toImmutableList());
    }

    private boolean isIncluded(@Nonnull OWLLiteral literal,
                               @Nonnull LangTagFilter langTagFilter) {
        return langTagFilter.isIncluded(literal.getLang());
    }
}