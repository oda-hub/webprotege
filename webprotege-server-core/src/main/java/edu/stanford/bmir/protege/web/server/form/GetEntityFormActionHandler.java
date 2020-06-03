package edu.stanford.bmir.protege.web.server.form;

import edu.stanford.bmir.protege.web.server.access.AccessManager;
import edu.stanford.bmir.protege.web.server.dispatch.AbstractProjectActionHandler;
import edu.stanford.bmir.protege.web.server.dispatch.ExecutionContext;
import edu.stanford.bmir.protege.web.server.frame.FrameComponentSessionRendererFactory;
import edu.stanford.bmir.protege.web.server.inject.ProjectComponent;
import edu.stanford.bmir.protege.web.shared.access.BuiltInAction;
import edu.stanford.bmir.protege.web.shared.form.GetEntityFormsAction;
import edu.stanford.bmir.protege.web.shared.form.GetEntityFormsResult;
import edu.stanford.bmir.protege.web.shared.form.field.GridControlOrdering;
import edu.stanford.bmir.protege.web.shared.project.ProjectId;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Inject;

import static com.google.common.collect.ImmutableList.toImmutableList;

/**
 * Matthew Horridge
 * Stanford Center for Biomedical Informatics Research
 * 2019-11-01
 */
public class GetEntityFormActionHandler extends AbstractProjectActionHandler<GetEntityFormsAction, GetEntityFormsResult> {

    @Nonnull
    private final ProjectId projectId;

    @Nonnull
    private final EntityFormManager formManager;

    @Nonnull
    private final ProjectComponent projectComponent;

    @Inject
    public GetEntityFormActionHandler(@Nonnull AccessManager accessManager,
                                      @Nonnull ProjectId projectId,
                                      @Nonnull EntityFormManager formManager,
                                      @Nonnull FrameComponentSessionRendererFactory sessionRendererFactory,
                                      @Nonnull ProjectComponent projectComponent) {
        super(accessManager);
        this.projectId = projectId;
        this.formManager = formManager;
        this.projectComponent = projectComponent;
    }

    @Nonnull
    @Override
    public GetEntityFormsResult execute(@Nonnull GetEntityFormsAction action,
                                        @Nonnull ExecutionContext executionContext) {
        var pageRequests = action.getFormPageRequests();
        var pageRequestIndex = FormPageRequestIndex.create(pageRequests);
        var entity = action.getEntity();
        var langTagFilter = action.getLangTagFilter();
        var formDataDtoBuilder = projectComponent.getFormDataComponent().createFormDataBuilder();
        var ordering = action.getGridControlOrdering();
        var forms = formManager.getFormDescriptors(entity, projectId)
                          .stream()
                          .map(formDescriptor -> formDataDtoBuilder.toFormData(entity, formDescriptor, pageRequestIndex, langTagFilter, ordering))
                          .collect(toImmutableList());
        return new GetEntityFormsResult(forms);
    }

    @Nonnull
    @Override
    public Class<GetEntityFormsAction> getActionClass() {
        return GetEntityFormsAction.class;
    }

    @Nullable
    @Override
    protected BuiltInAction getRequiredExecutableBuiltInAction() {
        return BuiltInAction.VIEW_PROJECT;
    }
}
