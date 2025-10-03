/*
 * Copyright 2025 HM Revenue & Customs
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

package v3.residentialPropertyDisposals.retrieveNonPpd

import cats.data.Validated.{Valid, Invalid}
import shared.controllers.validators.Validator
import shared.models.errors.MtdError
import v3.residentialPropertyDisposals.retrieveNonPpd.RetrieveNonPpdResidentialPropertyCgtSchema.{Def1, Def2}
import v3.residentialPropertyDisposals.retrieveNonPpd.def1.Def1_RetrieveNonPpdResidentialPropertyCgtValidator
import v3.residentialPropertyDisposals.retrieveNonPpd.def2.Def2_RetrieveNonPpdResidentialPropertyCgtValidator
import v3.residentialPropertyDisposals.retrieveNonPpd.model.request.RetrieveNonPpdResidentialPropertyCgtRequestData

import javax.inject.Inject

class RetrieveNonPpdResidentialPropertyCgtValidatorFactory @Inject() {

  def validator(nino: String, taxYear: String): Validator[RetrieveNonPpdResidentialPropertyCgtRequestData] = {
    val schema = RetrieveNonPpdResidentialPropertyCgtSchema.schemaFor(taxYear)

    schema match {
      case Valid(Def1)     => new Def1_RetrieveNonPpdResidentialPropertyCgtValidator(nino, taxYear)
      case Valid(Def2)     => new Def2_RetrieveNonPpdResidentialPropertyCgtValidator(nino, taxYear)
      case Invalid(errors) => Validator.returningErrors(errors)
    }

  }

}
