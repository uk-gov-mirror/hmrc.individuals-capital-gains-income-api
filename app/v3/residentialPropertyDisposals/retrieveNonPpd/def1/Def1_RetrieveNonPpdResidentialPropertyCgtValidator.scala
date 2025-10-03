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

package v3.residentialPropertyDisposals.retrieveNonPpd.def1

import cats.data.Validated
import shared.controllers.validators.Validator
import shared.controllers.validators.resolvers.ResolveNino
import shared.models.domain.TaxYear
import shared.models.errors.MtdError
import v3.residentialPropertyDisposals.retrieveNonPpd.def1.model.request.Def1_RetrieveNonPpdResidentialPropertyRequestData
import v3.residentialPropertyDisposals.retrieveNonPpd.model.request.RetrieveNonPpdResidentialPropertyCgtRequestData

import javax.inject.{Inject, Singleton}

@Singleton
class Def1_RetrieveNonPpdResidentialPropertyCgtValidator @Inject() (nino: String, taxYear: String)
    extends Validator[RetrieveNonPpdResidentialPropertyCgtRequestData] {

  override def validate: Validated[Seq[MtdError], RetrieveNonPpdResidentialPropertyCgtRequestData] =
    ResolveNino(nino).map(validNino => Def1_RetrieveNonPpdResidentialPropertyRequestData(validNino, TaxYear.fromMtd(taxYear)))

}
