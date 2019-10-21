import axios from 'axios'

import ExecutionResult from './interfaces/executionResult'
import JobResult from './interfaces/jobResult'
import { Stats } from './interfaces/stats'
import { NotificationRequest, NotificationType } from './interfaces/notificationRequest'
import WebhookCallback from './interfaces/webhookCallback'

import TransformationService from './interfaces/transformationService'
import SandboxExecutor from './interfaces/sandboxExecutor'

const VERSION = '0.0.2'

export default class JSTransformationService implements TransformationService {
  executor: SandboxExecutor

  constructor (executor: SandboxExecutor) {
    this.executor = executor
  }

  getVersion (): string {
    return VERSION
  }

  private executionTimeInMillis (func: () => ExecutionResult): [number, ExecutionResult] {
    const start = process.hrtime()
    const result = func()
    const hrresult = process.hrtime(start)
    const time = hrresult[0] * 1e3 + hrresult[1] / 1e6
    return [time, result]
  }

  executeJob (code: string, data: object): JobResult {
    const [time, result] = this.executionTimeInMillis(() =>
      this.executor.execute(code, data)
    )
    const stats: Stats = { executionTime: time }
    const jobResult: JobResult = { ...result, stats }
    return jobResult
  }

  async handleNotification (notificationRequest: NotificationRequest): Promise<void> {
    const success = this.executor.evaluate(notificationRequest.condition, notificationRequest.data)
    if (!success) { // no need to trigger notification
      return Promise.resolve()
    }

    // different notification types might be implemented later
    if (notificationRequest.type !== NotificationType.WEBHOOK) {
      throw new Error('notification type not implemented')
    }

    return this.executeWebhook(notificationRequest.callbackUrl, notificationRequest.dataLocation)
  }

  private async executeWebhook (callbackUrl: string, dataLocation: string): Promise<void> {
    const callbackObject: WebhookCallback = {
      location: dataLocation,
      timestamp: new Date(Date.now())
    }
    await axios.post(callbackUrl, callbackObject)
  }
}