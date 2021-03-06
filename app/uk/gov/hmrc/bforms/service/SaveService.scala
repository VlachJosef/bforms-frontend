/*
 * Copyright 2017 HM Revenue & Customs
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

package uk.gov.hmrc.bforms.service

import scala.concurrent.ExecutionContext
import uk.gov.hmrc.bforms.connectors.BformsConnector
import uk.gov.hmrc.bforms.models.{ FormData, FormId, FormTypeId, SaveResult, VerificationResult }
import uk.gov.hmrc.play.http.{ HeaderCarrier, HttpResponse }

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.libs.json._

object SaveService {

  def bformsConnector : BformsConnector = BformsConnector

  def getFormById(formTypeId: FormTypeId, version: String, formId: FormId)(implicit hc : HeaderCarrier) = {
    bformsConnector.getById(formTypeId, version, formId)
  }

  def saveFormData(formData: FormData, tolerant: Boolean)(implicit hc : HeaderCarrier): Future[SaveResult] = {
    bformsConnector.save(formData, tolerant)
  }

  def updateFormData(formId: FormId, formData: FormData, tolerant: Boolean)(implicit hc : HeaderCarrier): Future[SaveResult] = {
    bformsConnector.update(formId, formData, tolerant)
  }

  def sendSubmission(formTypeId: FormTypeId, formId: FormId)(implicit hc : HeaderCarrier): Future[HttpResponse] = {
    bformsConnector.sendSubmission(formTypeId, formId)
  }
}
