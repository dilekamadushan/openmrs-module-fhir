package org.openmrs.module.fhir.providers;

import ca.uhn.fhir.rest.annotation.Create;
import ca.uhn.fhir.rest.annotation.Delete;
import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.ResourceParam;
import ca.uhn.fhir.rest.annotation.Update;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.server.IResourceProvider;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Coding;
import org.hl7.fhir.dstu3.model.IdType;
import org.hl7.fhir.dstu3.model.OperationOutcome;
import org.hl7.fhir.dstu3.model.RelatedPerson;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.openmrs.module.fhir.api.util.FHIRConstants;
import org.openmrs.module.fhir.resources.FHIRRelatedPersonResource;

public class RestfulRelatedPersonProvider implements IResourceProvider {

    private FHIRRelatedPersonResource relatedPersonResource;

    public RestfulRelatedPersonProvider() {
        relatedPersonResource = new FHIRRelatedPersonResource();
    }

    @Override
    public Class<? extends IBaseResource> getResourceType() {
        return RelatedPerson.class;
    }

    /**
     * Get related person by unique id
     *
     * @param theId object containing the id
     */
    @Read()
    public RelatedPerson getResourceById(@IdParam IdType theId) {
        return relatedPersonResource.getByUniqueId(theId);
    }

    /**
     * Delete related person by unique id
     *
     * @param theId object containing the id
     */
    @Delete()
    public void deleteRelatedPerson(@IdParam IdType theId) {
        relatedPersonResource.deleteRelatedPerson(theId);
    }

    /**
     * Update related person by unique id
     *
     * @param theId object containing the id
     */
    @Update
    public MethodOutcome updateRelatedPerson(@ResourceParam RelatedPerson relatedPerson, @IdParam IdType theId) {
        MethodOutcome retVal = new MethodOutcome();
        OperationOutcome outcome = new OperationOutcome();
        relatedPersonResource.updateRelatedPerson(theId.getIdPart(), relatedPerson);
        CodeableConcept concept = new CodeableConcept();
        Coding coding = concept.addCoding();
        coding.setDisplay("Related person is successfully updated with id " + relatedPerson.getId());
        outcome.addIssue().setDetails(concept);
        retVal.setOperationOutcome(outcome);
        return retVal;
    }

    /**
     * Create related person
     *
     * @param relatedPerson fhir related person object
     */
    @Create
    public MethodOutcome createRelatedPerson(@ResourceParam RelatedPerson relatedPerson) {
        relatedPerson = relatedPersonResource.createRelatedPerson(relatedPerson);
        MethodOutcome retVal = new MethodOutcome();
        retVal.setId(new IdType(FHIRConstants.RELATED_PERSON, relatedPerson.getId()));
        OperationOutcome outcome = new OperationOutcome();
        CodeableConcept concept = new CodeableConcept();
        Coding coding = concept.addCoding();
        coding.setDisplay("Related person is successfully created with id " + relatedPerson.getId());
        outcome.addIssue().setDetails(concept);
        retVal.setOperationOutcome(outcome);
        return retVal;
    }
}
