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

import common.errors.SourceFormatError
import shared.controllers.EndpointLogContext
import shared.models.domain.{Nino, TaxYear}
import shared.models.errors.*
import shared.models.outcomes.ResponseWrapper
import shared.services.ServiceSpec
import uk.gov.hmrc.http.HeaderCarrier
import v3.residentialPropertyDisposals.retrieveNonPpd.def1.model.request.Def1_RetrieveNonPpdResidentialPropertyRequestData
import v3.residentialPropertyDisposals.retrieveNonPpd.def1.model.response.Def1_RetrieveNonPpdResidentialPropertyCgtResponse
import v3.residentialPropertyDisposals.retrieveNonPpd.model.request.RetrieveNonPpdResidentialPropertyCgtRequestData

import scala.concurrent.Future

class RetrieveNonPpdResidentialPropertyCgtServiceSpec extends ServiceSpec {

  private val nino    = "AA112233A"
  private val taxYear = "2019-20"

  val request: RetrieveNonPpdResidentialPropertyCgtRequestData = Def1_RetrieveNonPpdResidentialPropertyRequestData(
    nino = Nino(nino),
    taxYear = TaxYear.fromMtd(taxYear)
  )

  val response: Def1_RetrieveNonPpdResidentialPropertyCgtResponse = Def1_RetrieveNonPpdResidentialPropertyCgtResponse(
    customerAddedDisposals = None
  )

  trait Test extends MockRetrieveNonPpdResidentialPropertyCgtConnector {

    implicit val hc: HeaderCarrier              = HeaderCarrier()
    implicit val logContext: EndpointLogContext = EndpointLogContext("controller", "RetrieveNonPpdResidentialPropertyCgt")

    val service: RetrieveNonPpdResidentialPropertyCgtService = new RetrieveNonPpdResidentialPropertyCgtService(
      connector = mockRetrieveNonPpdResidentialPropertyCgtConnector
    )

  }

  "RetrieveNonPpdResidentialPropertyCgtService" when {

    "retrieve" must {
      "return correct result for a success" in new Test {
        val outcome = Right(ResponseWrapper(correlationId, response))

        MockRetrieveNonPpdResidentialPropertyCgtConnector
          .retrieve(request)
          .returns(Future.successful(outcome))

        await(service.retrieve(request)) shouldBe outcome
      }

      "map errors according to spec" when {

        def serviceError(downstreamErrorCode: String, error: MtdError): Unit =
          s"a $downstreamErrorCode error is returned from the service" in new Test {

            MockRetrieveNonPpdResidentialPropertyCgtConnector
              .retrieve(request)
              .returns(Future.successful(Left(ResponseWrapper(correlationId, DownstreamErrors.single(DownstreamErrorCode(downstreamErrorCode))))))

            await(service.retrieve(request)) shouldBe Left(ErrorWrapper(correlationId, error))
          }

        val errors = List(
          ("INVALID_TAXABLE_ENTITY_ID", NinoFormatError),
          ("INVALID_TAX_YEAR", TaxYearFormatError),
          ("INVALID_CORRELATIONID", InternalError),
          ("INVALID_CORRELATION_ID", InternalError),
          ("NO_DATA_FOUND", NotFoundError),
          ("SERVER_ERROR", InternalError),
          ("SERVICE_UNAVAILABLE", InternalError),
          ("INVALID_VIEW", SourceFormatError),
          ("TAX_YEAR_NOT_SUPPORTED", RuleTaxYearNotSupportedError)
        )

        errors.foreach(args => (serviceError).tupled(args))
      }
    }
  }

}
