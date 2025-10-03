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

import play.api.Configuration
import play.api.mvc.Result
import shared.config.MockSharedAppConfig
import shared.controllers.{ControllerBaseSpec, ControllerTestRunner}
import shared.models.domain.{Nino, TaxYear}
import shared.models.errors.*
import shared.models.outcomes.ResponseWrapper
import shared.services.{MockEnrolmentsAuthService, MockMtdIdLookupService}
import shared.utils.MockIdGenerator
import v3.residentialPropertyDisposals.retrieveNonPpd.def1.fixture.Def1_RetrieveNonPpdResidentialPropertyCgtControllerFixture.{mtdJson, responseModel}
import v3.residentialPropertyDisposals.retrieveNonPpd.def1.model.request.Def1_RetrieveNonPpdResidentialPropertyRequestData
import v3.residentialPropertyDisposals.retrieveNonPpd.model.request.RetrieveNonPpdResidentialPropertyCgtRequestData

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RetrieveNonPpdResidentialPropertyCgtControllerSpec
    extends ControllerBaseSpec
    with ControllerTestRunner
    with MockEnrolmentsAuthService
    with MockMtdIdLookupService
    with MockRetrieveNonPpdResidentialPropertyCgtService
    with MockRetrieveNonPpdResidentialPropertyCgtValidatorFactory
    with MockIdGenerator
    with MockSharedAppConfig {

  val taxYear: String = "2024-25"

  val requestData: RetrieveNonPpdResidentialPropertyCgtRequestData = Def1_RetrieveNonPpdResidentialPropertyRequestData(
    nino = Nino(validNino),
    taxYear = TaxYear.fromMtd(taxYear)
  )

  "retrieve" should {
    "return a successful response with status 200 (OK)" when {
      "given a valid request" in new Test {
        willUseValidator(returningSuccess(requestData))

        MockRetrieveNonPpdResidentialPropertyCgtService
          .retrieve(requestData)
          .returns(Future.successful(Right(ResponseWrapper(correlationId, responseModel))))

        runOkTest(
          expectedStatus = OK,
          maybeExpectedResponseBody = Some(mtdJson)
        )
      }
    }

    "return the error as per spec" when {
      "the parser validation fails" in new Test {
        willUseValidator(returning(NinoFormatError))

        runErrorTest(NinoFormatError)
      }

      "the service returns an error" in new Test {
        willUseValidator(returningSuccess(requestData))

        MockRetrieveNonPpdResidentialPropertyCgtService
          .retrieve(requestData)
          .returns(Future.successful(Left(ErrorWrapper(correlationId, RuleTaxYearNotSupportedError))))

        runErrorTest(RuleTaxYearNotSupportedError)
      }
    }
  }

  private trait Test extends ControllerTest {

    val controller: RetrieveNonPpdResidentialPropertyCgtController = new RetrieveNonPpdResidentialPropertyCgtController(
      authService = mockEnrolmentsAuthService,
      lookupService = mockMtdIdLookupService,
      validatorFactory = mockRetrieveNonPpdResidentialPropertyCgtValidatorFactory,
      service = mockRetrieveNonPpdResidentialPropertyCgtService,
      cc = cc,
      idGenerator = mockIdGenerator
    )

    MockedSharedAppConfig.featureSwitchConfig.anyNumberOfTimes() returns Configuration(
      "supporting-agents-access-control.enabled" -> true
    )

    MockedSharedAppConfig.endpointAllowsSupportingAgents(controller.endpointName).anyNumberOfTimes() returns false

    protected def callController(): Future[Result] = controller.retrieve(validNino, taxYear)(fakeGetRequest)

  }

}
