package niklasu.speedtester.measurements

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.google.inject.Singleton


class MeasurementsModule : AbstractModule() {
    @Provides
    @Singleton
    private fun getMeasurements(): Measurements {
        return Measurements()
    }

}