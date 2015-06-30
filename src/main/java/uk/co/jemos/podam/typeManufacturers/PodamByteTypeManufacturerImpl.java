package uk.co.jemos.podam.typeManufacturers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.jemos.podam.api.DataProviderStrategy;
import uk.co.jemos.podam.common.PodamByteValue;
import uk.co.jemos.podam.typeManufacturers.wrappers.TypeManufacturerParamsWrapper;

import java.lang.annotation.Annotation;

/**
 * Default byte type manufacturer.
 *
 * Created by tedonema on 17/05/2015.
 *
 * @since 6.0.0.RELEASE
 */
public class PodamByteTypeManufacturerImpl extends AbstractTypeManufacturer {

    /** The application logger */
    private static final Logger LOG = LogManager.getLogger(PodamByteTypeManufacturerImpl.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public Byte getType(TypeManufacturerParamsWrapper wrapper) {

        super.checkWrapperIsValid(wrapper);

        DataProviderStrategy strategy = wrapper.getDataProviderStrategy();

        Byte retValue = null;

        for (Annotation annotation : wrapper.getAttributeMetadata().getAttributeAnnotations()) {

            if (PodamByteValue.class.isAssignableFrom(annotation.getClass())) {
                PodamByteValue intStrategy = (PodamByteValue) annotation;

                String numValueStr = intStrategy.numValue();
                if (null != numValueStr && !"".equals(numValueStr)) {
                    try {

                        retValue = Byte.valueOf(numValueStr);

                    } catch (NumberFormatException nfe) {
                        String errMsg = "The precise value: "
                                + numValueStr
                                + " cannot be converted to a byte type. An exception will be thrown.";
                        LOG.error(errMsg);
                        throw new IllegalArgumentException(errMsg, nfe);
                    }
                } else {
                    byte minValue = intStrategy.minValue();
                    byte maxValue = intStrategy.maxValue();

                    // Sanity check
                    if (minValue > maxValue) {
                        maxValue = minValue;
                    }

                    retValue = strategy.getByteInRange(minValue, maxValue,
                            wrapper.getAttributeMetadata());
                }

                break;

            }
        }

        if (retValue == null) {
            retValue = strategy.getByte(wrapper.getAttributeMetadata());
        }

        return retValue;
    }
}
