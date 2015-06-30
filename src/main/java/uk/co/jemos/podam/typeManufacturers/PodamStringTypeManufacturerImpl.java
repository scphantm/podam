package uk.co.jemos.podam.typeManufacturers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.jemos.podam.api.AttributeMetadata;
import uk.co.jemos.podam.api.DataProviderStrategy;
import uk.co.jemos.podam.common.PodamStringValue;
import uk.co.jemos.podam.typeManufacturers.wrappers.TypeManufacturerParamsWrapper;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Default String type manufacturer.
 *
 * Created by tedonema on 17/05/2015.
 *
 * @since 6.0.0.RELEASE
 */
public class PodamStringTypeManufacturerImpl extends AbstractTypeManufacturer {

    /** The application logger */
    private static final Logger LOG = LogManager.getLogger(PodamStringTypeManufacturerImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public String getType(TypeManufacturerParamsWrapper wrapper) {

        super.checkWrapperIsValid(wrapper);

        DataProviderStrategy strategy = wrapper.getDataProviderStrategy();

        String retValue = null;

        AttributeMetadata attributeMetadata = wrapper.getAttributeMetadata();

        List<Annotation> annotations = attributeMetadata.getAttributeAnnotations();

        if (annotations == null || annotations.isEmpty()) {

            retValue = strategy.getStringValue(attributeMetadata);

        } else {

            for (Annotation annotation : annotations) {

                if (!PodamStringValue.class.isAssignableFrom(annotation
                        .getClass())) {
                    continue;
                }

                // A specific value takes precedence over the length
                PodamStringValue podamAnnotation = (PodamStringValue) annotation;

                if (podamAnnotation.strValue() != null
                        && podamAnnotation.strValue().length() > 0) {

                    retValue = podamAnnotation.strValue();

                } else {

                    retValue = strategy.getStringOfLength(
                            podamAnnotation.length(), attributeMetadata);

                }

            }

            if (retValue == null) {
                retValue = strategy.getStringValue(attributeMetadata);
            }

        }

        return retValue;
    }
}
