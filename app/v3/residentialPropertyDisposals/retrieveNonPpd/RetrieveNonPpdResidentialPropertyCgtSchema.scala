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

import cats.data.Validated
import cats.data.Validated.Valid
import play.api.libs.json.Reads
import shared.controllers.validators.resolvers.ResolveTaxYearMinimum
import shared.models.domain.TaxYear
import shared.models.errors.MtdError
import shared.schema.DownstreamReadable
import v3.residentialPropertyDisposals.retrieveNonPpd.def1.model.response.Def1_RetrieveNonPpdResidentialPropertyCgtResponse
import v3.residentialPropertyDisposals.retrieveNonPpd.def2.model.response.Def2_RetrieveNonPpdResidentialPropertyCgtResponse
import v3.residentialPropertyDisposals.retrieveNonPpd.model.response.RetrieveNonPpdResidentialPropertyCgtResponse

import scala.math.Ordering.Implicits.infixOrderingOps

sealed trait RetrieveNonPpdResidentialPropertyCgtSchema extends DownstreamReadable[RetrieveNonPpdResidentialPropertyCgtResponse]

object RetrieveNonPpdResidentialPropertyCgtSchema {

  case object Def1 extends RetrieveNonPpdResidentialPropertyCgtSchema {
    type DownstreamResp = Def1_RetrieveNonPpdResidentialPropertyCgtResponse
    val connectorReads: Reads[DownstreamResp] = Def1_RetrieveNonPpdResidentialPropertyCgtResponse.reads
  }

  case object Def2 extends RetrieveNonPpdResidentialPropertyCgtSchema {
    type DownstreamResp = Def2_RetrieveNonPpdResidentialPropertyCgtResponse
    val connectorReads: Reads[DownstreamResp] = Def2_RetrieveNonPpdResidentialPropertyCgtResponse.reads
  }

  def schemaFor(taxYearString: String): Validated[Seq[MtdError], RetrieveNonPpdResidentialPropertyCgtSchema] =
    ResolveTaxYearMinimum(TaxYear.fromMtd("2019-20"))(taxYearString) andThen schemaFor

  def schemaFor(taxYear: TaxYear): Validated[Seq[MtdError], RetrieveNonPpdResidentialPropertyCgtSchema] = {
    if (taxYear >= TaxYear.fromMtd("2025-26")) Valid(Def2) else Valid(Def1)
  }

}
